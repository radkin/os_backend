package co.inajar.oursponsors.services.fec;

import co.inajar.oursponsors.dbos.entities.SponsorSenator;
import co.inajar.oursponsors.dbos.entities.campaigns.Committee;
import co.inajar.oursponsors.dbos.entities.campaigns.Donation;
import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.dbos.repos.CommitteeRepo;
import co.inajar.oursponsors.dbos.repos.SponsorSenatorsRepo;
import co.inajar.oursponsors.dbos.repos.fec.DonationRepo;
import co.inajar.oursponsors.dbos.repos.fec.SponsorsRepo;
import co.inajar.oursponsors.dbos.repos.propublica.SenatorRepo;
import co.inajar.oursponsors.models.fec.FecCommitteeDonor;
import co.inajar.oursponsors.models.fec.MiniDonationResponse;
import co.inajar.oursponsors.models.fec.SponsorResponse;
import co.inajar.oursponsors.models.opensecrets.CampaignResponse;
import co.inajar.oursponsors.models.opensecrets.CommitteeResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

@Service
public class CommitteesApiImpl implements CommitteesApiManager {

    private static final String INVALID_CHAMBER_NAME = "Invalid chamber please use either senator or congress";
    private static final String SENATOR_ID_NOT_FOUND = "No Senator found by ID: {}";
    private final Logger logger = LoggerFactory.getLogger(CommitteesApiImpl.class);
    @Value("${fec.inajar.token.secret}")
    private String fecApiKey;
    @Autowired
    private CommitteeRepo committeeRepo;
    @Autowired
    private SponsorsRepo sponsorsRepo;
    @Autowired
    private DonationRepo donationRepo;
    @Autowired
    private SenatorRepo senatorRepo;
    @Autowired
    private SponsorSenatorsRepo sponsorSenatorsRepo;

    @Override
    public List<FecCommitteeDonor> getFecCommitteeDonors(String committeeId, Integer twoYearTransactionPeriod) {
        // https://api.open.fec.gov/v1/schedules/schedule_a/
        // ?sort_hide_null=false&sort_nulls_last=false&data_type=processed&committee_id=C00701128
        // &two_year_transaction_period=2020&sort=-contribution_receipt_date&per_page=30
        // &sort=-contributor_aggregate_ytd

        var path = "/v1/schedules/schedule_a";

        var webClient = getClient().get()
                .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("sort_hide_null", false)
                        .queryParam("sort_nulls_last", false)
                        .queryParam("data_type", "processed")
                        .queryParam("committee_id", committeeId)
                        .queryParam("two_year_transaction_period", twoYearTransactionPeriod)
                        .queryParam("sort", "contribution_receipt_date")
                        .queryParam("per_page", 30)
                        .queryParam("sort", "contributor_aggregate_ytd")
                        .queryParam("api_key", fecApiKey)
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
            return mapFecCommitteeDonorsToModel(webClient.block());
        }
        return Collections.emptyList();
    }

    @Override
    public CampaignResponse getSenatorCampaignListResponse(Senator senator) {
        CampaignResponse campaignResponse = new CampaignResponse();
        String cmte = getFecCommitteeIdFromProPublicaCandidateId(senator.getFecCandidateId());
        Committee committee = createSenatorCommittee(senator, cmte);
        Map<String, List<FecCommitteeDonor>> cmteFecDonors = fetchCmteFecDonors(committee, senator.getNextElection());
        List<Sponsor> newSponsors = processDonorsAndGetNewSponsors(senator.getId(), senator.getProPublicaId(), cmteFecDonors);
        List<CommitteeResponse> committeeResponses = Stream.of(committee)
                .map(CommitteeResponse::new)
                .toList();
        List<SponsorResponse> sponsorResponses = newSponsors.parallelStream()
                .map(SponsorResponse::new)
                .toList();
        List<MiniDonationResponse> donationResponses = newSponsors.parallelStream()
                .flatMap(sponsor -> sponsor.getDonations().stream())
                .map(MiniDonationResponse::new)
                .toList();
        campaignResponse.setCommittees(committeeResponses);
        campaignResponse.setSponsors(sponsorResponses);
        campaignResponse.setDonations(donationResponses);
        return campaignResponse;
    }

    @Override
    public CampaignResponse getCongressCampaignListResponse(Congress congress) {
        CampaignResponse campaignResponse = new CampaignResponse();
        String cmte = getFecCommitteeIdFromProPublicaCandidateId(congress.getFecCandidateId());
        Committee committee = createCongressCommittee(congress, cmte);
        Map<String, List<FecCommitteeDonor>> cmteFecDonors = fetchCmteFecDonors(committee, congress.getNextElection());
        List<Sponsor> newSponsors = processDonorsAndGetNewSponsors(congress.getId(), congress.getProPublicaId(), cmteFecDonors);
        List<CommitteeResponse> committeeResponses = Stream.of(committee)
                .map(CommitteeResponse::new)
                .toList();
        List<SponsorResponse> sponsorResponses = newSponsors.parallelStream()
                .map(SponsorResponse::new)
                .toList();
        List<MiniDonationResponse> donationResponses = newSponsors.parallelStream()
                .flatMap(sponsor -> sponsor.getDonations().stream())
                .map(MiniDonationResponse::new)
                .toList();
        campaignResponse.setCommittees(committeeResponses);
        campaignResponse.setSponsors(sponsorResponses);
        campaignResponse.setDonations(donationResponses);
        return campaignResponse;
    }

    private WebClient getClient() {
        return WebClient.builder()
                .clientConnector((connector()))
                .exchangeStrategies(ExchangeStrategies.builder().codecs(clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(1000000)).build())
                .baseUrl("https://api.open.fec.gov")
                .build();
    }

    private List<FecCommitteeDonor> mapFecCommitteeDonorsToModel(String response) {
        var mappedDonors = new ArrayList<FecCommitteeDonor>();
        var objectMapper = new ObjectMapper();
        try {
            var tree = objectMapper.readTree(response);
            var donorsResponse = tree.get("results");
            for (JsonNode jsonNode : donorsResponse) {
                try {
                    var donor = objectMapper.treeToValue(jsonNode, FecCommitteeDonor.class);
                    mappedDonors.add(donor);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return mappedDonors;
    }

    private ClientHttpConnector connector() {
        return new ReactorClientHttpConnector(HttpClient.from(TcpClient.newConnection()));
    }

    private String getFecCommitteeIdFromProPublicaCandidateId(String fecCandidateId) {
        // https://api.open.fec.gov/v1/candidates/search/?page=1&per_page=20
        // &candidate_id=S2CA00955&sort=name&sort_hide_null=false
        // &sort_null_only=false&sort_nulls_last=false&api_key=DEMO_KEY

        var path = "/v1/candidates/search";
        var webClient = getClient().get()
                .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("page", "1")
                        .queryParam("per_page", 20)
                        .queryParam("candidate_id", fecCandidateId)
                        .queryParam("sort", "name")
                        .queryParam("sort_hide_null", false)
                        .queryParam("sort_null_only", false)
                        .queryParam("sort_nulls_last", false)
                        .queryParam("api_key", fecApiKey)
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
            return getCandidateCommitteeId(webClient.block());
        }
        return "";
    }

    private Committee createSenatorCommittee(Senator senator, String cmte) {
        var committee = new Committee();
        committee.setPpId(senator.getProPublicaId());
        committee.setTwoYearTransactionPeriod(calcTwoYearTransactionPeriod(senator.getNextElection()));
        committee.setFecCommitteeId(cmte);
        return committeeRepo.save(committee);
    }

    private Committee createCongressCommittee(Congress congress, String cmte) {
        var committee = new Committee();
        committee.setPpId(congress.getProPublicaId());
        committee.setTwoYearTransactionPeriod(calcTwoYearTransactionPeriod(congress.getNextElection()));
        committee.setFecCommitteeId(cmte);
        return committeeRepo.save(committee);
    }

    private Integer calcTwoYearTransactionPeriod(String nextElection) {
        return Integer.parseInt(nextElection) - 2;
    }

    private Map<String, List<FecCommitteeDonor>> fetchCmteFecDonors(Committee committee, String nextElection) {
        var donorMap = new HashMap<String, List<FecCommitteeDonor>>();
        var donors = getFecCommitteeDonors(committee.getFecCommitteeId(), calcTwoYearTransactionPeriod(nextElection));
        donorMap.put("", donors);
        return donorMap;
    }

    private List<Sponsor> processDonorsAndGetNewSponsors(Long osId, String proPublicaId, Map<String, List<FecCommitteeDonor>> cmteFecDonors) {
        List<Sponsor> newSponsors = new ArrayList<>();
        List<Donation> donations = new ArrayList<>();
        cmteFecDonors.forEach((cmte, donorList) -> donorList.forEach(d -> {
            Optional<Sponsor> possibleExistingSponsor = getSponsorByName(d.getContributorName());
            Sponsor sponsor;
            if (possibleExistingSponsor.isEmpty()) {
                sponsor = mapFecDonorToSponsor(d, osId);
                newSponsors.add(sponsor);
            } else {
                BigDecimal possibleExistingSponsorYtd = possibleExistingSponsor.get().getContributorAggregateYtd();
                BigDecimal newSponsorYtd = new BigDecimal(d.getContributorAggregateYtd());
                sponsor = possibleExistingSponsor.get();
                if (newSponsorYtd.compareTo(possibleExistingSponsorYtd) > 0) {
                    sponsor.setContributorAggregateYtd(possibleExistingSponsorYtd);
                }
            }
            donations.add(mapFecDonorToDonation(d, sponsor, proPublicaId));
        }));
        return newSponsors;
    }

    private String getCandidateCommitteeId(String response) {
        String committeeId = "";
        var objectMapper = new ObjectMapper();
        try {
            var tree = objectMapper.readTree(response);
            JsonNode searchResponse = tree.get("results");
            for (JsonNode jsonNode : searchResponse) {
                var principalCommittees = jsonNode.get("principal_committees");
                for (JsonNode pc : principalCommittees) {
                    var jnCmte = pc.get("committee_id");
                    committeeId = objectMapper.treeToValue(jnCmte, String.class);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return committeeId;
    }

    private Optional<Sponsor> getSponsorByName(String name) {
        return Optional.ofNullable(sponsorsRepo.findByContributorName(name));
    }

    private Sponsor mapFecDonorToSponsor(FecCommitteeDonor donor, Long osId) {
        var chamber = "senator";
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
            var possibleSenator = Optional.of(senatorRepo.getById(osId));
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
                logger.info(SENATOR_ID_NOT_FOUND, osId);
            }
        } else {
            logger.info(INVALID_CHAMBER_NAME);
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

}
