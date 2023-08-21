package co.inajar.oursponsors.services.opensecrets;

import co.inajar.oursponsors.dbos.entities.SponsorSenator;
import co.inajar.oursponsors.dbos.entities.campaigns.Committee;
import co.inajar.oursponsors.dbos.entities.campaigns.Donation;
import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.dbos.entities.candidates.Contributor;
import co.inajar.oursponsors.dbos.entities.candidates.Sector;
import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.dbos.repos.CommitteeRepo;
import co.inajar.oursponsors.dbos.repos.SponsorSenatorsRepo;
import co.inajar.oursponsors.dbos.repos.fec.DonationRepo;
import co.inajar.oursponsors.dbos.repos.fec.SponsorsRepo;
import co.inajar.oursponsors.dbos.repos.opensecrets.ContributorRepo;
import co.inajar.oursponsors.dbos.repos.opensecrets.SectorRepo;
import co.inajar.oursponsors.dbos.repos.propublica.CongressRepo;
import co.inajar.oursponsors.dbos.repos.propublica.SenatorRepo;
import co.inajar.oursponsors.models.fec.FecCommitteeDonor;
import co.inajar.oursponsors.models.fec.MiniDonationResponse;
import co.inajar.oursponsors.models.fec.SponsorResponse;
import co.inajar.oursponsors.models.opensecrets.CampaignResponse;
import co.inajar.oursponsors.models.opensecrets.CommitteeRequest;
import co.inajar.oursponsors.models.opensecrets.CommitteeResponse;
import co.inajar.oursponsors.models.opensecrets.contributor.OpenSecretsContributor;
import co.inajar.oursponsors.models.opensecrets.sector.OpenSecretsSector;
import co.inajar.oursponsors.services.fec.CommitteesApiManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CandidatesApiImpl implements CandidatesApiManager {
    private static final String NO_CONTRIBUTOR_FOUND = "No Contributor found! {}";
    private static final String OPEN_SECRETS_CLIENT_PROBLEM = "Problem with client request to OpenSecrets.org CID: {}";
    private static final String UNABLE_TO_SLEEP = "Unable to sleep {}";
    private static final String CALL_LIMIT_REACHED = "Very Likely OpenSecretsOrg saying - call limit has been reached";

    @Autowired
    private SectorRepo sectorRepo;

    @Autowired
    private ContributorRepo contributorRepo;

    @Autowired
    private SenatorRepo senatorRepo;

    @Autowired
    private CongressRepo congressRepo;

    @Autowired
    private CommitteeRepo committeeRepo;

    @Autowired
    private SponsorsRepo sponsorsRepo;

    @Autowired
    private DonationRepo donationRepo;

    @Autowired
    private SponsorSenatorsRepo sponsorSenatorsRepo;

    @Autowired
    private CommitteesApiManager committeesApiManager;

    private Logger logger = LoggerFactory.getLogger(CandidatesApiImpl.class);

    @Value("${opensecrets.inajar.token.secret}")
    private String opensecretsApiKey;

    @Override
    public List<OpenSecretsSector> getSectorsListResponse(Integer part) {
        var openSecretsSectors = new ArrayList<OpenSecretsSector>();
        // get a list of all cids in propublica table
        // Todo: This should be a one off SQL query not a pull of all Members
        var chunk = gatherCids();
        var chunkPart = chunk.get(part);
        for (var cid : chunkPart) {
            // one CID to many sectors
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                logger.error(UNABLE_TO_SLEEP, e);
            }
            var possibleSectors = Optional.ofNullable(getOpenSecretsSector(cid));
            possibleSectors.ifPresent(openSecretsSectors::addAll);
        }

        return openSecretsSectors;
    }

    private List<List<String>> gatherCids() {
        var senators = getSenators();
        var congress = getCongress();

        List<String> senatorCIDs = senators.parallelStream()
                .map(Senator::getCrpId)
                .toList();

        List<String> congressCIDs = congress.parallelStream()
                .map(Congress::getCrpId)
                .toList();

        List<String> allCIDs = Stream
                .of(senatorCIDs, congressCIDs)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .toList();

        // break this up into chunks for REST API "job". Fine-grained control dealing with 200 request limit/day
        final int chunkSize = 99;
        final AtomicInteger counter = new AtomicInteger();
        return allCIDs.stream()
                .collect(Collectors.groupingBy(it -> counter.getAndIncrement() / chunkSize))
                .values()
                .stream()
                .toList();
    }

    private List<Senator> getSenators() {
        return senatorRepo.findAll();
    }

    private List<Congress> getCongress() {
        return congressRepo.findAll();
    }

    private WebClient getClient() {
        return WebClient.builder()
                .clientConnector((connector()))
                .exchangeStrategies(ExchangeStrategies.builder().codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(1000000)).build())
                .baseUrl("https://www.opensecrets.org")
                .build();
    }

    private List<OpenSecretsSector> mapOpenSecretsSectorToModel(String response, String cid, Integer cycle) {
        var mappedSectors = new ArrayList<OpenSecretsSector>();
        var objectMapper = new ObjectMapper();
        try {
            var tree = objectMapper.readTree(response);
            var sectorsResponse = tree.get("response").get("sectors").get("sector");
            for (JsonNode jsonNode : sectorsResponse) {
                var sectorAttributes = jsonNode.get("@attributes");
                try {
                    var sector = objectMapper.treeToValue(sectorAttributes, OpenSecretsSector.class);
                    sector.setCid(cid);
                    sector.setCycle(cycle);
                    mappedSectors.add(sector);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return mappedSectors;
    }

    private ClientHttpConnector connector() {
        return new ReactorClientHttpConnector(HttpClient.from(TcpClient.newConnection()));
    }

    @Override
    public List<Sector> mapOpenSecretsResponseToSectors(List<OpenSecretsSector> sectors) {
        // for now every sector is a new one. We are not set up for updates. Delete all prior to refresh
        return sectors.parallelStream()
                .map(this::createSector)
                .toList();
    }

    @Override
    public List<OpenSecretsSector> getOpenSecretsSector(String cid) {
        // ?method=candSector&cid=N00040675&cycle=2022%0A&output=json&apikey=${KEY}
        // ToDo: we will need to add the cycle dynamically
        var cycle = 2022;
        var path = "/api/";
        var webClient = getClient().get()
                .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("method", "candSector")
                        .queryParam("cid", cid)
                        .queryParam("cycle", String.valueOf(cycle))
                        .queryParam("output", "json")
                        .queryParam("apikey", opensecretsApiKey)
                        .build())
                .retrieve()
                .onStatus(
                        HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.class,
                        ex -> ex.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(ex))
                .retry(3);
        var response = Optional.ofNullable(webClient.block());
        if (response.isPresent()) {
            return mapOpenSecretsSectorToModel(webClient.block(), cid, cycle);
        }
        return Collections.emptyList();
    }

    @Override
    public List<OpenSecretsContributor> getContributorsListResponse(Integer part) {
        var openSecretsContributors = new ArrayList<OpenSecretsContributor>();
        // get a list of all cids in propublica table
        // Todo: This should be a one off SQL query not a pull of all Members
        var chunk = gatherCids();
        var chunkPart = chunk.get(part);
        for (var cid : chunkPart) {
            // one CID to many sectors
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                logger.error(UNABLE_TO_SLEEP, e);
            }
            var possibleContributors = Optional.ofNullable(getOpenSecretsContributor(cid));
            possibleContributors.ifPresent(openSecretsContributors::addAll);
        }

        return openSecretsContributors;
    }

    private List<OpenSecretsContributor> mapOpenSecretsContributorToModel(String response, String cid, Integer cycle) {
        var mappedContributors = new ArrayList<OpenSecretsContributor>();
        var objectMapper = new ObjectMapper();
        try {
            var tree = objectMapper.readTree(response);
            var contributorResponse = tree.get("response").get("contributors").get("contributor");
            for (JsonNode jsonNode : contributorResponse) {
                var contributorAttributes = jsonNode.get("@attributes");
                try {
                    var possibleContributor = Optional.ofNullable(objectMapper.treeToValue(contributorAttributes, OpenSecretsContributor.class));
                    if (possibleContributor.isPresent()) {
                        var contributor = possibleContributor.get();
                        contributor.setCid(cid);
                        contributor.setCycle(cycle);
                        mappedContributors.add(contributor);
                    } else {
                        logger.error(NO_CONTRIBUTOR_FOUND, response);
                    }
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return mappedContributors;
    }

    @Override
    public List<Contributor> mapOpenSecretsResponseToContributors(List<OpenSecretsContributor> contributors) {
        // for now every contributor is a new one. We are not set up for updates. Delete all prior to refresh
        return contributors.parallelStream()
                .map(this::createContributor)
                .toList();
    }

    @Override
    public List<OpenSecretsContributor> getOpenSecretsContributor(String cid) {
        var contributorsList = new ArrayList<OpenSecretsContributor>();
        var cycle = 2022;
        var path = "/api";
        var webClient = getClient().get()
                .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("method", "candContrib")
                        .queryParam("cid", cid)
                        .queryParam("cycle", String.valueOf(cycle))
                        .queryParam("output", "json")
                        .queryParam("apikey", opensecretsApiKey)
                        .build())
                .retrieve()
                .onStatus(
                        HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.class,
                        ex -> ex.getRawStatusCode() == 404 || ex.getRawStatusCode() == 400 ? Mono.empty() : Mono.error(ex))
                .retry(1);
        var response = Optional.ofNullable(webClient.block());
        if (response.isPresent()) {
            var list = mapOpenSecretsContributorToModel(webClient.block(), cid, cycle);
            contributorsList.addAll(list);
        } else {
            logger.debug(CALL_LIMIT_REACHED);
            logger.error(OPEN_SECRETS_CLIENT_PROBLEM, cid);
        }

        return contributorsList;
    }

    @Override
    public CampaignResponse getCampaignListResponse(CommitteeRequest data) {
        var cmtes = new ArrayList<String>();
        try {
            Elements links = new Elements();
            String url = "https://www.opensecrets.org/" + data.getTwoYearTransactionPeriod()
                    + "-presidential-race/candidate?id=" + data.getCrpId();
            Document doc = Jsoup.connect(url).get();
            Elements content = doc.select("#main > div.Main-wrap.l-padding.u-mt4.u-mb4 > div > div > div.l-primary > div");
            Elements elements = content.first().getElementsByClass("DataTable");
            for (Element el : elements) {
                links = el.getElementsByTag("a");
            }
            for (Element hrefel : links) {
                Element link = hrefel.select("a").first();
                String href = link.attr("href");

                cmtes.add(extractCmteFromHref(href));
            }
        } catch (Error | IOException e) {
            logger.error("Oops" + e);
        }
        // populate committee entries for each of the results
//        System.out.println(cmtes);

        // first make sure there is not an existing entry
        // 1. find committees by ppId (limit should be 3 entries).
        // 2. check if the two year transaction period is the same.
        // if Yes, it's a duplicate. If not, add a new row.

        /* SKIPPING THE ABOVE CHECKS FOR NOW */
        List<Committee> committees = cmtes.parallelStream()
                .map(cmte -> createCommittee(data, cmte))
                .collect(Collectors.toList());

        System.out.println(committees);

        // for each committee get a list of donors and filter to $50k or $100K ?

        // cmte -> List of donors HashMap
        // NOTE: when mapped to our model donor = sponsor
        var cmteFecDonors = new HashMap<String, List<FecCommitteeDonor>>();
        for (var cmte : cmtes) {
            List<FecCommitteeDonor> donors =
                    committeesApiManager.getFecCommitteeDonors(cmte, data.getTwoYearTransactionPeriod());
            cmteFecDonors.put(cmte, donors);
        }

        System.out.println(cmteFecDonors);

        // use cmteFecDonors to populate sponsor and donation table
        List<Sponsor> newSponsors = new ArrayList<Sponsor>();
        List<Donation> donations = new ArrayList<Donation>();
        cmteFecDonors.forEach((cmte, donorList) -> {
            System.out.println(cmte);
            System.out.println("------------------------------------");
            donorList.forEach(d -> {
                var sponsor = new Sponsor();
                // we are using the name, but it could be a problem.
                var possibleExistingSponsor = getSponsorByName(d.getContributorName());
                if (!possibleExistingSponsor.isPresent()) {
                    // we need to know if crp_id is for a Senator or Congress and we need their ID
                    sponsor = mapFecDonorToSponsor(d, data.getChamber(), Long.valueOf(data.getOsId()));
                    newSponsors.add(sponsor);
                } else {
                    BigDecimal possibleExistingSponsorYtd = possibleExistingSponsor.get().getContributorAggregateYtd();
                    BigDecimal newSponsorYtd = new BigDecimal(d.getContributorAggregateYtd());
                    sponsor = possibleExistingSponsor.get();
                    if (newSponsorYtd.compareTo(possibleExistingSponsorYtd) == 1) {
                        sponsor.setContributorAggregateYtd(possibleExistingSponsorYtd);
                    }
                }

                // create a donation. ToDo: check for dupes
                donations.add(mapFecDonorToDonation(d, sponsor, data.getPpId()));

                // Do something for the campaign response.


//                System.out.println("Name:" + d.getContributorName() + "yearToDate:" + d.getContributorAggregateYtd());
            });
        });

        var committeeResponses = committees.parallelStream()
                .map(CommitteeResponse::new)
                .collect(Collectors.toList());

        var sponsorResponses = newSponsors.parallelStream()
                .map(SponsorResponse::new)
                .collect(Collectors.toList());

        var donationResponses = donations.parallelStream()
                .map(MiniDonationResponse::new)
                .collect(Collectors.toList());

        var campaignResponse = new CampaignResponse();
        campaignResponse.setCommittees(committeeResponses);
        campaignResponse.setSponsors(sponsorResponses);
        campaignResponse.setDonations(donationResponses);

        return campaignResponse;
    }

    @Override
    public List<Committee> getCommittees() {
        return committeeRepo.findAll();
    }

    private static String extractCmteFromHref(String href) {
        String[] parts = href.split("\\?"); // Split by "?"
        if (parts.length > 1) {
            String query = parts[1];
            String[] params = query.split("&"); // Split by "&"
            for (String param : params) {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2 && "cmte".equals(keyValue[0]) || "strID".equals(keyValue[0])) {
                    return keyValue[1];
                }
            }
        }
        return null; // Return null if "cmte" not found
    }

    private Committee createCommittee(CommitteeRequest data, String cmte) {
        var committee = new Committee();
        committee.setPpId(data.getPpId());
        committee.setTwoYearTransactionPeriod(data.getTwoYearTransactionPeriod());
        committee.setFecCommitteeId(cmte);
        return committeeRepo.save(committee);
    }

    private Optional<Sponsor> getSponsorByName(String name) {
        return Optional.ofNullable(sponsorsRepo.findByContributorName(name));
    }

    private Sponsor mapFecDonorToSponsor(FecCommitteeDonor donor, String chamber, Long osId) {
        var newSponsor = new Sponsor();
        var receiptAmount = new BigDecimal(donor.getContributionReceiptAmount());
        newSponsor.setContributionReceiptAmount(receiptAmount);
        newSponsor.setContributionReceiptDate(donor.getContributionReceiptDate());
        var aggregateAmount = new BigDecimal(donor.getContributorAggregateYtd());
        newSponsor.setContributorAggregateYtd(aggregateAmount);
        newSponsor.setContributorCity(donor.getContributorCity());
        newSponsor.setContributorEmployer(donor.getContributorEmployer());
        newSponsor.setContributorFirstName(donor.getContributorFirstName());
        newSponsor.setContributorLastName(donor.getContributorLastName());
        newSponsor.setContributorMiddleName(donor.getContributorMiddleName());
        newSponsor.setContributorName(donor.getContributorName());
        newSponsor.setContributorOccupation(donor.getContributorOccupation());
        newSponsor.setContributorState(donor.getContributorState());
        newSponsor.setContributorStreet1(donor.getContributorStreet1());
        newSponsor.setContributorStreet2(donor.getContributorStreet2());
        newSponsor.setContributorZip(donor.getContributorZip());

        if (chamber.equals("senator")) {
            var possibleSenator = Optional.ofNullable(senatorRepo.getById(osId));
            if (possibleSenator.isPresent()) {
                // add new sponsor
                Senator senator = possibleSenator.get();
                sponsorsRepo.save(newSponsor);
                // add sponsorSenator ManyToMany
                SponsorSenator sponsorSenator = new SponsorSenator();
                sponsorSenator.setSponsor(newSponsor);
                sponsorSenator.setSenator(senator);
                sponsorSenatorsRepo.save(sponsorSenator);
            } else {
                logger.debug("No Senator found by ID {}", osId);
            }
        } else {
            logger.debug("Invalid chamber please use either senator or congress");
        }
        return newSponsor;
    }

    private Donation mapFecDonorToDonation(FecCommitteeDonor donor, Sponsor sponsor, String ppId) {
        var newDonation = new Donation();
        newDonation.setDateOfDonation(LocalDate.parse(donor.getContributionReceiptDate()));
        BigDecimal donation = new BigDecimal(donor.getContributionReceiptAmount());
        newDonation.setAmount(donation);
        newDonation.setSponsor(sponsor);
        newDonation.setPpId(ppId);
        return donationRepo.save(newDonation);
    }

    private Contributor createContributor(OpenSecretsContributor openSecretsContributor) {
        var newContributor = new Contributor();
        var nc = setContributor(newContributor, openSecretsContributor);
        return contributorRepo.save(nc);
    }

    private Contributor setContributor(Contributor contributor, OpenSecretsContributor osc) {
        contributor.setCid(osc.getCid());
        contributor.setCycle(osc.getCycle());
        contributor.setOrgName(osc.getOrgName());
        contributor.setContributorId(osc.getContributorId());
        contributor.setIndivs(Integer.valueOf(osc.getIndivs()));
        contributor.setPacs(Integer.valueOf(osc.getPacs()));
        contributor.setTotal(Integer.valueOf(osc.getTotal()));
        return contributor;
    }

    private Sector createSector(OpenSecretsSector openSecretsSector) {
        var newSector = new Sector();
        var ns = setSector(newSector, openSecretsSector);
        return sectorRepo.save(ns);
    }

    private Sector setSector(Sector sector, OpenSecretsSector oss) {
        sector.setCid(oss.getCid());
        sector.setCycle(oss.getCycle());
        sector.setSectorName(oss.getSectorName());
        sector.setSectorId(oss.getSectorId());
        sector.setIndivs(Integer.valueOf(oss.getIndivs()));
        sector.setPacs(Integer.valueOf(oss.getPacs()));
        sector.setTotal(Integer.valueOf(oss.getTotal()));
        return sector;
    }

}
