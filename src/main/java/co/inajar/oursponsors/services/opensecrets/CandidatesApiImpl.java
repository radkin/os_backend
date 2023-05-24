package co.inajar.oursponsors.services.opensecrets;

import co.inajar.oursponsors.dbOs.entities.candidates.Contributor;
import co.inajar.oursponsors.dbOs.entities.candidates.Sector;
import co.inajar.oursponsors.dbOs.entities.chambers.Congress;
import co.inajar.oursponsors.dbOs.entities.chambers.Senator;
import co.inajar.oursponsors.dbOs.repos.opensecrets.ContributorRepo;
import co.inajar.oursponsors.dbOs.repos.opensecrets.SectorRepo;
import co.inajar.oursponsors.dbOs.repos.propublica.CongressRepo;
import co.inajar.oursponsors.dbOs.repos.propublica.SenatorRepo;
import co.inajar.oursponsors.models.opensecrets.contributor.OpenSecretsContributor;
import co.inajar.oursponsors.models.opensecrets.sector.OpenSecretsSector;
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

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import java.util.stream.Collectors;

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

    private Logger logger = LoggerFactory.getLogger(CandidatesApiImpl.class);

    @Value("${opensecrets.inajar.token.secret}")
    private String opensecretsApiKey;

    private List<Senator> getSenators() { return senatorRepo.findAll(); }

    private List<Congress> getCongress() { return congressRepo.findAll(); }

    private WebClient getClient() {
        return WebClient.builder()
                .clientConnector((connector()))
                .exchangeStrategies(ExchangeStrategies.builder().codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer.defaultCodecs().maxInMemorySize(1000000);}).build())
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

    private ClientHttpConnector connector() {
        return new ReactorClientHttpConnector(HttpClient.from(TcpClient.newConnection()));
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
        if (response.isPresent()) { return mapOpenSecretsSectorToModel(webClient.block(), cid, cycle); }
        return null;
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

    private Sector createSector(OpenSecretsSector openSecretsSector) {
        var newSector = new Sector();
        var ns = setSector(newSector, openSecretsSector);
        return sectorRepo.save(ns);
    }

    private Contributor createContributor(OpenSecretsContributor openSecretsContributor) {
        var newContributor = new Contributor();
        var nc = setContributor(newContributor, openSecretsContributor);
        return contributorRepo.save(nc);
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

    @Override
    public List<Sector> mapOpenSecretsResponseToSectors(List<OpenSecretsSector> sectors) {
        // for now every sector is a new one. We are not set up for updates. Delete all prior to refresh
        return sectors.parallelStream()
                .map(s -> createSector(s))
                .toList();
    }

    @Override
    public List<Contributor> mapOpenSecretsResponseToContributors(List<OpenSecretsContributor> contributors) {
        // for now every contributor is a new one. We are not set up for updates. Delete all prior to refresh
        return contributors.parallelStream()
                .map(c -> createContributor(c))
                .toList();
    }

}
