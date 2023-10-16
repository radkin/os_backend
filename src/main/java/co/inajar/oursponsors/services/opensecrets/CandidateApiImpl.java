package co.inajar.oursponsors.services.opensecrets;

import co.inajar.oursponsors.dbos.entities.campaigns.Committee;
import co.inajar.oursponsors.dbos.entities.campaigns.Donation;
import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.dbos.entities.candidates.Contributor;
import co.inajar.oursponsors.dbos.entities.candidates.Sector;
import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.dbos.repos.CommitteeRepo;
import co.inajar.oursponsors.dbos.repos.fec.DonationRepo;
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
import co.inajar.oursponsors.services.fec.CommitteeManager;
import co.inajar.oursponsors.services.fec.SponsorManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.channel.ChannelOption;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class CandidateApiImpl implements CandidateApiManager {
    private static final String NO_CONTRIBUTOR_FOUND = "No Contributor found! {}";
    private static final String OPEN_SECRETS_CLIENT_PROBLEM = "Problem with client request to OpenSecrets.org CID: {}";
    private static final String UNABLE_TO_SLEEP = "Unable to sleep";
    private static final String CALL_LIMIT_REACHED = "Very Likely OpenSecretsOrg saying - call limit has been reached";
    private static final String SCRAPE_OPEN_SECRETS_CAMPAIGN_PAGE = "Unable to scrape opensecrets presidential race campaign data";

    // temporary
//    private static final String NON_PRESIDENTIAL_CANDIDATE = "Cannot scrape opensecrets for non-presidential candidate fundraising data";
    private final Logger logger = LoggerFactory.getLogger(CandidateApiImpl.class);

    @Autowired
    private SponsorManager sponsorManager;
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
    private DonationRepo donationRepo;
    @Autowired
    private CommitteeManager committeeManager;
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
        HttpClient httpClient = HttpClient.create();
        httpClient.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        httpClient.option(ChannelOption.SO_KEEPALIVE, true);
        return new ReactorClientHttpConnector(httpClient);
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
            } catch (InterruptedException e) {
                logger.error(UNABLE_TO_SLEEP, e);
                Thread.currentThread().interrupt();
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
            logger.info(CALL_LIMIT_REACHED);
            logger.error(OPEN_SECRETS_CLIENT_PROBLEM, cid);
        }

        return contributorsList;
    }

    // This only works for the following Candidates from 2020:
    // PP_ids = H000273, B001267, B001288, G000555, K000367, S000033, W000817
    @Override
    public CampaignResponse getPresidentialCampaignListResponse(CommitteeRequest data) {
        List<String> cmtes = scrapeCmteUrls(data);

        List<Committee> committees = cmtes.parallelStream()
                .map(cmte -> createCommittee(data, cmte))
                .toList();

        Map<String, List<FecCommitteeDonor>> cmteFecDonors = fetchCmteFecDonors(committees, data);

        List<Sponsor> newSponsors = processDonorsAndGetNewSponsors(data, cmteFecDonors);

        List<CommitteeResponse> committeeResponses = committees.parallelStream()
                .map(CommitteeResponse::new)
                .toList();

        List<SponsorResponse> sponsorResponses = newSponsors.parallelStream()
                .map(SponsorResponse::new)
                .toList();

        List<MiniDonationResponse> donationResponses = newSponsors.parallelStream()
                .flatMap(sponsor -> sponsor.getDonations().stream())
                .map(MiniDonationResponse::new)
                .toList();

        CampaignResponse campaignResponse = new CampaignResponse();
        campaignResponse.setCommittees(committeeResponses);
        campaignResponse.setSponsors(sponsorResponses);
        campaignResponse.setDonations(donationResponses);
        return campaignResponse;
    }

    // This works for everyone




    private List<String> scrapeCmteUrls(CommitteeRequest data) {
        List<String> cmtes = new ArrayList<>();
        try {
            String url = "https://www.opensecrets.org/" + data.getTwoYearTransactionPeriod()
                    + "-presidential-race/candidate?id=" + data.getCrpId();
            Document doc = Jsoup.connect(url).get();
            new Elements();
            Elements links;
            try {
                links = doc.select("#main > div.Main-wrap.l-padding.u-mt4.u-mb4 > div > div > div.l-primary > div")
                        .first()
                        .getElementsByClass("DataTable")
                        .select("a");
                links.forEach(hrefel -> {
                    String href = hrefel.attr("href");
                    cmtes.add(extractCmteFromHref(href));
                });
            } catch (NullPointerException e) {
                logger.error("Failed to get links", e);
            }


        } catch (IOException e) {
            logger.error(SCRAPE_OPEN_SECRETS_CAMPAIGN_PAGE, e);
        }
        return cmtes;
    }

    private Committee createCommittee(CommitteeRequest data, String cmte) {
        var committee = new Committee();
        committee.setPpId(data.getPpId());
        committee.setTwoYearTransactionPeriod(data.getTwoYearTransactionPeriod());
        committee.setFecCommitteeId(cmte);
        return committeeRepo.save(committee);
    }

    private Map<String, List<FecCommitteeDonor>> fetchCmteFecDonors(List<Committee> committees, CommitteeRequest data) {
        return committees.parallelStream()
                .collect(Collectors.toMap(
                        Committee::getFecCommitteeId,
                        cmte -> committeeManager.getFecCommitteeDonors(cmte.getFecCommitteeId(), data.getTwoYearTransactionPeriod())
                ));
    }

    // Note: this is not the same as SponsorsManager.processDonorsAndGetNewSponsors.
    // ToDo: rename or refactor to use the one in sponsorsManager
    private List<Sponsor> processDonorsAndGetNewSponsors(CommitteeRequest data, Map<String, List<FecCommitteeDonor>> cmteFecDonors) {
        List<Sponsor> newSponsors = new ArrayList<>();
        List<Donation> donations = new ArrayList<>();
        cmteFecDonors.forEach((cmte, donorList) -> donorList.forEach(d -> {
            Optional<Sponsor> possibleExistingSponsor = sponsorManager.getSponsorByName(d.getContributorName());
            Sponsor sponsor;
            if (possibleExistingSponsor.isEmpty()) {
                sponsor = sponsorManager.mapFecDonorToSponsor(d, data.getChamber(), data.getOsId());
                newSponsors.add(sponsor);
            } else {
                BigDecimal possibleExistingSponsorYtd = possibleExistingSponsor.get().getContributorAggregateYtd();
                BigDecimal newSponsorYtd = new BigDecimal(d.getContributorAggregateYtd());
                sponsor = possibleExistingSponsor.get();
                if (newSponsorYtd.compareTo(possibleExistingSponsorYtd) > 0) {
                    sponsor.setContributorAggregateYtd(possibleExistingSponsorYtd);
                }
            }
            donations.add(mapFecDonorToDonation(d, sponsor, data.getPpId()));
        }));
        return newSponsors;
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
