package co.inajar.oursponsors.services.fec;

import co.inajar.oursponsors.dbos.entities.campaigns.Committee;
import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.dbos.repos.CommitteeRepo;
import co.inajar.oursponsors.models.fec.FecCommitteeDonor;
import co.inajar.oursponsors.models.fec.MiniDonationResponse;
import co.inajar.oursponsors.models.fec.SponsorResponse;
import co.inajar.oursponsors.models.opensecrets.CampaignResponse;
import co.inajar.oursponsors.models.opensecrets.CommitteeResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.ClientCodecConfigurer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.TcpClient;

import java.util.*;
import java.util.function.Consumer;

@Service
public class CommitteeImpl implements CommitteeManager {

    @Value("${fec.inajar.token.secret}")
    private String fecApiKey;

    @Autowired
    private CommitteeRepo committeeRepo;

    @Autowired
    private SponsorManager sponsorManager;

    @Autowired
    private DonationManager donationManager;

    @Override
    public List<Committee> getCommittees() {
        return committeeRepo.findAll();
    }

    @Override
    public List<FecCommitteeDonor> getFecCommitteeDonors(String committeeId, Integer twoYearTransactionPeriod) {
        WebClient webClient = createWebClient();
        return fetchFecCommitteeDonors(webClient, committeeId, twoYearTransactionPeriod);
    }

    @Override
    public CampaignResponse getSenatorCampaignListResponse(Senator senator) {
        String cmte = getFecCommitteeIdFromProPublicaCandidateId(senator.getFecCandidateId());
        Committee committee = createCommittee(senator, cmte);
        Map<String, List<FecCommitteeDonor>> cmteFecDonors = fetchCmteFecDonors(committee);
        List<Sponsor> newSponsors = sponsorManager.processDonorsAndGetNewSponsors("senator", senator.getId(), senator.getProPublicaId(), cmteFecDonors);
        var committeeResponse = Collections.singletonList(new CommitteeResponse(committee));
        return createCampaignResponse(committeeResponse, newSponsors);
    }

    @Override
    public CampaignResponse getCongressCampaignListResponse(Congress congress) {
        String cmte = getFecCommitteeIdFromProPublicaCandidateId(congress.getFecCandidateId());
        Committee committee = createCommittee(congress, cmte);
        Map<String, List<FecCommitteeDonor>> cmteFecDonors = fetchCmteFecDonors(committee);
        List<Sponsor> newSponsors = sponsorManager.processDonorsAndGetNewSponsors("congress", congress.getId(), congress.getProPublicaId(), cmteFecDonors);
        var committeeResponse = Collections.singletonList(new CommitteeResponse(committee));
        return createCampaignResponse(committeeResponse, newSponsors);
    }

    private CampaignResponse createCampaignResponse(List<CommitteeResponse> committeeResponse, List<Sponsor> newSponsors) {
        CampaignResponse campaignResponse = new CampaignResponse();
        campaignResponse.setCommittees(committeeResponse);
        campaignResponse.setSponsors(mapSponsorResponses(newSponsors));
        campaignResponse.setDonations(donationManager.mapDonationResponses(newSponsors));
        return campaignResponse;
    }

    private WebClient createWebClient() {
        return WebClient.builder()
                .clientConnector(createClientHttpConnector())
                .exchangeStrategies(createExchangeStrategies())
                .baseUrl("https://api.open.fec.gov")
                .build();
    }

    private ClientHttpConnector createClientHttpConnector() {
        return new ReactorClientHttpConnector(createReactorHttpClient());
    }

    private HttpClient createReactorHttpClient() {
        return HttpClient.from(TcpClient.newConnection());
    }

    private ExchangeStrategies createExchangeStrategies() {
        return ExchangeStrategies.builder()
                .codecs(configureCodecs())
                .build();
    }

    private Consumer<ClientCodecConfigurer> configureCodecs() {
        return clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(1000000);
    }

    private List<FecCommitteeDonor> fetchFecCommitteeDonors(WebClient webClient, String committeeId, Integer twoYearTransactionPeriod) {
        var path = "/v1/schedules/schedule_a";
        return Optional.ofNullable(
                        webClient.get()
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
                                .retry(3)
                                .block())
                .map(this::mapFecCommitteeDonorsToModel)
                .orElse(Collections.emptyList());
    }

    private List<FecCommitteeDonor> mapFecCommitteeDonorsToModel(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode results = objectMapper.readTree(response).get("results");
            List<FecCommitteeDonor> mappedDonors = new ArrayList<>();

            for (JsonNode jsonNode : results) {
                try {
                    FecCommitteeDonor donor = objectMapper.treeToValue(jsonNode, FecCommitteeDonor.class);
                    mappedDonors.add(donor);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }

            return mappedDonors;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFecCommitteeIdFromProPublicaCandidateId(String fecCandidateId) {
        var path = "/v1/candidates/search";
        WebClient webClient = createWebClient();
        return Optional.ofNullable(
                        webClient.get()
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
                                .retry(3)
                                .block())
                .map(this::getCandidateCommitteeId)
                .orElse("");
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

    public Committee createCommittee(Object entity, String cmte) {
        var committee = new Committee();
        if (entity instanceof Senator senator) {
            committee.setPpId(senator.getProPublicaId());
        } else if (entity instanceof Congress congress) {
            committee.setPpId(congress.getProPublicaId());
        }
        committee.setTwoYearTransactionPeriod(calcTwoYearTransactionPeriod(entity));
        committee.setFecCommitteeId(cmte);
        return committeeRepo.save(committee);
    }

    private Integer calcTwoYearTransactionPeriod(Object entity) {
        if (entity instanceof Senator senator) {
            return Integer.parseInt(senator.getNextElection()) - 2;
        } else if (entity instanceof Congress congress) {
            return Integer.parseInt(congress.getNextElection()) - 2;
        }
        return 0;
    }

    private Map<String, List<FecCommitteeDonor>> fetchCmteFecDonors(Committee committee) {
        var donorMap = new HashMap<String, List<FecCommitteeDonor>>();
        var donors = fetchFecCommitteeDonors(createWebClient(), committee.getFecCommitteeId(), calcTwoYearTransactionPeriod(committee));
        donorMap.put("", donors);
        return donorMap;
    }

    private List<SponsorResponse> mapSponsorResponses(List<Sponsor> sponsors) {
        return sponsors.parallelStream()
                .map(SponsorResponse::new)
                .toList();
    }
}
