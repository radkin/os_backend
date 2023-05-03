package co.inajar.oursponsors.services.opensecrets;

import co.inajar.oursponsors.dbOs.entities.candidates.Sector;
import co.inajar.oursponsors.dbOs.entities.chambers.Congress;
import co.inajar.oursponsors.dbOs.entities.chambers.Senator;
import co.inajar.oursponsors.dbOs.repos.opensecrets.SectorRepo;
import co.inajar.oursponsors.dbOs.repos.propublica.CongressRepo;
import co.inajar.oursponsors.dbOs.repos.propublica.SenatorRepo;
import co.inajar.oursponsors.models.opensecrets.sector.SectorResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collection;

@Service
public class CandidatesApiImpl implements CandidatesApiManager {

    @Autowired
    private SectorRepo sectorRepo;

//    @Autowired
//    private ContributionRepo contributionRepo;

    @Autowired
    private SenatorRepo senatorRepo;

    @Autowired
    private CongressRepo congressRepo;

    private Logger logger = LoggerFactory.getLogger(CandidatesApiImpl.class);

    @Value("${opensecrets.inajar.token.secret}")
    private String opensecretsApiKey;

    private List<Sector> getSectors() { return sectorRepo.findAll(); }

    private List<Senator> getSenators() { return senatorRepo.findAll(); }

    private List<Congress> getCongress() { return congressRepo.findAll(); }

    private WebClient getClient() {
        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder().codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer.defaultCodecs().maxInMemorySize(1000000);}).build())
                .baseUrl("https://www.opensecrets.org")
                .build();
    }

    private List<SectorResponse> mapCandSectorResponseToModel(String response) {
        var mappedSectors = new ArrayList<SectorResponse>();
        var objectMapper = new ObjectMapper();
        try {
            var tree = objectMapper.readTree(response);
            var candSectors = tree.get("response").get("sectors").get("sector");
            for (JsonNode jsonNode : candSectors) {
                var sectorAttributes = jsonNode.get("@attributes");
                try {
                    mappedSectors.add(objectMapper.treeToValue(sectorAttributes, SectorResponse.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return mappedSectors;
    }

    private List<SectorResponse> getCandSectorResponse(String cid) {
        // ?method=candSector&cid=N00040675&cycle=2022%0A&output=json&apikey=${KEY}
        // ToDo: we will need to add the cycle dynamically
        var path = "/api/";
        var webClient = getClient().get()
                .uri(uriBuilder -> uriBuilder.path(path)
                        .queryParam("method", "candSector")
                        .queryParam("cid", cid)
                        .queryParam("cycle", "2022")
                        .queryParam("output", "json")
                        .queryParam("apikey", opensecretsApiKey)
                        .build())
                .retrieve()
                .onStatus(
                        HttpStatus.INTERNAL_SERVER_ERROR::equals,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .bodyToMono(String.class);

        return mapCandSectorResponseToModel(webClient.block());
    }
    @Override
    public List<String> getAllCandSectorsFromOpenSecrets() {
        // get a list of all cids in propublica table
        // Todo: This should be a one off SQL query not a pull of all Members
        var senators = getSenators();
        var congress = getCongress();

        List<String> senatorCIDs = senators.parallelStream()
                .map(Senator::getCrpId)
                .collect(Collectors.toList());

        List<String> congressCIDs = congress.parallelStream()
                .map(Congress::getCrpId)
                .collect(Collectors.toList());

        List<String> allCIDs = Stream
                .of(senatorCIDs, congressCIDs)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        for (var cid : allCIDs) {
            if (Objects.equals(cid, "N00003535") || Objects.equals(cid, "N00045974")) {
                System.out.println("We have a match for either N00003535 Sherrod Brown");
                System.out.println("Or N00045974 Lauren Boebert");
                var candSectorResponse = getCandSectorResponse(cid);
                System.out.println(candSectorResponse);
            }
            System.out.println("getting open secrets sectors for CID " + cid);
        }

        // run getCandSectorResponse for each cid
        // run mapOpenSecretsResponseToSectors for each response
        return allCIDs;
    }



    // before populating our DBs we need to use the client to get it from opensecrets
//    private List<Sector> mapOpenSecretsResponseToSectors(CandSectorResponse candSectorResponse) {
//        var sectorCids = getSectors().parallelStream()
//                .map(Sector::getCid)
//                .collect(Collectors.toList());
//
//        // get the cid & cycle(year) and (of course) sector object from candSectorResponse
//
//        // mark these sectors as "NEW" if there are no existing cid/cycle(year)/sector combinations that match
    // NOTE: cycle is datatype year for Java and INTEGER for postgreSQL
//
//        // mark these sectors as "UPDATE" if there is an existing cid/cycle(year)/sector combination
//
//
//
//    }
}
