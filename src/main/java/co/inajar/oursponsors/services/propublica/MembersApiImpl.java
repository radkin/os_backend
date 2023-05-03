package co.inajar.oursponsors.services.propublica;

import co.inajar.oursponsors.dbOs.entities.chambers.Congress;
import co.inajar.oursponsors.dbOs.repos.propublica.CongressRepo;
import co.inajar.oursponsors.helpers.DateTimeConversion;
import co.inajar.oursponsors.dbOs.entities.chambers.Senator;
import co.inajar.oursponsors.dbOs.repos.propublica.SenatorRepo;
import co.inajar.oursponsors.models.propublica.congress.ProPublicaCongress;
import co.inajar.oursponsors.models.propublica.senator.ProPublicaSenator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class MembersApiImpl implements MembersApiManager {

    @Autowired
    private SenatorRepo senatorRepo;

    @Autowired
    private CongressRepo congressRepo;
    
    private Logger logger = LoggerFactory.getLogger(MembersApiImpl.class);

    @Value("${propublica.inajar.token.secret}")
    private String propublicaApiKey;

    private List<Senator> getSenators() { return senatorRepo.findAll(); }

    private List<Congress> getCongress() { return congressRepo.findAll(); }

    //Senate
    @Override
    public List<ProPublicaSenator> getSenatorsListResponse() {
//        Integer page = 1;
        // ToDo: this is hard set for session 117 and it needs to advance to 118 in the next election cycle
        var path = String.format("congress/v1/117/senate/members.json");
        var webClient = getClient().get()
                .uri(uriBuilder -> uriBuilder.path(path)
//                        .queryParam("q", page.toString())
                        .build())
                .retrieve()
                .bodyToMono(String.class);

        return mapSenatorResponseToModel(webClient.block());
    }


    private List<ProPublicaSenator> mapSenatorResponseToModel(String response) {
        var mappedSenators = new ArrayList<ProPublicaSenator>();
        var objectMapper = new ObjectMapper();
        try {
            var tree = objectMapper.readTree(response);
            var members = tree.get("results").get(0).get("members");
            for (JsonNode jsonNode : members) {
                try {
                    mappedSenators.add(objectMapper.treeToValue(jsonNode, ProPublicaSenator.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return mappedSenators;
    }

    private WebClient getClient() {
        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder().codecs(clientCodecConfigurer -> {
                    clientCodecConfigurer.defaultCodecs().maxInMemorySize(1000000);}).build())
                .baseUrl("https://api.propublica.org")
                .defaultHeader("X-API-Key", propublicaApiKey)
                .build();
    }

    private Senator setSenator(Senator senator, ProPublicaSenator sr) {
        senator.setProPublicaId(sr.getId());
        senator.setTitle(sr.getTitle());
        senator.setShortTitle(sr.getShortTitle());
        senator.setApiUri(sr.getApiUri());
        senator.setFirstName(sr.getFirstName());
        senator.setMiddleName(sr.getMiddleName());
        senator.setLastName(sr.getLastName());
        senator.setSuffix(sr.getSuffix());

        LocalDate dob = DateTimeConversion.formatStringForLocalDate(sr.getDateOfBirth());
        senator.setDateOfBirth(dob);

        senator.setGender(sr.getGender());
        senator.setParty(sr.getParty());
        senator.setLeadershipRole(sr.getLeadershipRole());
        senator.setTwitterAccount(sr.getTwitterAccount());
        senator.setFacebookAccount(sr.getFacebookAccount());
        senator.setYoutubeAccount(sr.getYoutubeAccount());
        senator.setGovtrackId(sr.getGovtrackId());
        senator.setCspanId(sr.getCspanId());
        senator.setVotesmartId(sr.getVotesmartId());
        senator.setIcpsrId(sr.getIcpsrId());
        senator.setCrpId(sr.getCrpId());
        senator.setGoogleEntityId(sr.getGoogleEntityId());
        senator.setFecCandidateId(sr.getFecCandidateId());
        senator.setUrl(sr.getUrl());
        senator.setRssUrl(sr.getRssUrl());
        senator.setContactForm(sr.getContactForm());
        senator.setInOffice(sr.getInOffice());
        senator.setDwNominate(sr.getDwNominate());
        senator.setSeniority(sr.getSeniority());
        senator.setNextElection(sr.getNextElection());
        senator.setTotalVotes(sr.getTotalVotes());
        senator.setMissedVotes(sr.getMissedVotes());
        senator.setTotalPresent(sr.getTotalPresent());

        LocalDateTime lastUpdate = DateTimeConversion.formatStringForLocalDateTime(sr.getLastUpdated());
        senator.setLastUpdated(lastUpdate);

        senator.setOcdId(sr.getOcdId());
        senator.setOffice(sr.getOffice());
        senator.setPhone(sr.getPhone());
        senator.setState(sr.getState());
        senator.setSenateClass(sr.getSenateClass());
        senator.setStateRank(sr.getStateRank());
        senator.setLisId(sr.getLisId());
        senator.setMissedVotesPct(sr.getMissedVotesPct());
        senator.setVotesWithPartyPct(sr.getVotesWithPartyPct());
        senator.setVotesAgainstPartyPct(sr.getVotesAgainstPartyPct());
        return senator;
    }

    private Senator createSenator(ProPublicaSenator proPublicaSenator) {
        var newSenator = new Senator();
        var bp = setSenator(newSenator, proPublicaSenator);
        return senatorRepo.save(bp);
    }

    private Optional<Senator> getSenatorByPpId(String proPublicaId) {
        return senatorRepo.findFirstSenatorByProPublicaId(proPublicaId);
    }
    private Senator updateSenator(ProPublicaSenator proPublicaSenator) {
        var possibleSenator = getSenatorByPpId(proPublicaSenator.getId());
        if (possibleSenator.isPresent()) {
            var senator = possibleSenator.get();
            var bp = setSenator(senator, proPublicaSenator);
            return senatorRepo.save(bp);
        } else {
            logger.error("No Blog Post with ID {}", proPublicaSenator.getId());
            return null;
        }

    }

    public List<Senator> mapPropublicaResponseToSenators(List<ProPublicaSenator> senators) {
        var senatorPPIds = getSenators().parallelStream()
                .map(Senator::getProPublicaId)
                .collect(Collectors.toList());

        var newSenators = senators.stream()
                .filter(p -> !senatorPPIds.contains(p.getId()))
                .collect(Collectors.toList());

        var updatePosts = senators.stream()
                .filter(p -> senatorPPIds.contains(p.getId()))
                .collect(Collectors.toList());

        var mergedList = new ArrayList<Senator>();
        var createList = newSenators.parallelStream()
                .map(s -> createSenator(s))
                .collect(Collectors.toList());
        var updateList = updatePosts.parallelStream()
                .map(s -> updateSenator(s))
                .collect(Collectors.toList());

        if (!createList.isEmpty()) mergedList.addAll(createList);
        if (!updateList.isEmpty()) mergedList.addAll(updateList);

        return mergedList;

    }

    // Congress
    @Override
    public List<ProPublicaCongress> getCongressListResponse() {
//        Integer page = 1;
        var path = String.format("congress/v1/117/house/members.json");
        var webClient = getClient().get()
                .uri(uriBuilder -> uriBuilder.path(path)
//                        .queryParam("q", page.toString())
                        .build())
                .retrieve()
                .bodyToMono(String.class);

        return mapCongressResponseToModel(webClient.block());
    }


    private List<ProPublicaCongress> mapCongressResponseToModel(String response) {
        var mappedCongresss = new ArrayList<ProPublicaCongress>();
        var objectMapper = new ObjectMapper();
        try {
            var tree = objectMapper.readTree(response);
            var members = tree.get("results").get(0).get("members");
            for (JsonNode jsonNode : members) {
                try {
                    mappedCongresss.add(objectMapper.treeToValue(jsonNode, ProPublicaCongress.class));
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return mappedCongresss;
    }

    private Congress setCongress(Congress congress, ProPublicaCongress co) {
        congress.setProPublicaId(co.getId());
        congress.setTitle(co.getTitle());
        congress.setShortTitle(co.getShortTitle());
        congress.setApiUri(co.getApiUri());
        congress.setFirstName(co.getFirstName());
        congress.setMiddleName(co.getMiddleName());
        congress.setLastName(co.getLastName());
        congress.setSuffix(co.getSuffix());

        LocalDate dob = DateTimeConversion.formatStringForLocalDate(co.getDateOfBirth());
        congress.setDateOfBirth(dob);

        congress.setGender(co.getGender());
        congress.setParty(co.getParty());
        congress.setLeadershipRole(co.getLeadershipRole());
        congress.setTwitterAccount(co.getTwitterAccount());
        congress.setFacebookAccount(co.getFacebookAccount());
        congress.setYoutubeAccount(co.getYoutubeAccount());
        congress.setGovtrackId(co.getGovtrackId());
        congress.setCspanId(co.getCspanId());
        congress.setVotesmartId(co.getVotesmartId());
        congress.setIcpsrId(co.getIcpsrId());
        congress.setCrpId(co.getCrpId());
        congress.setGoogleEntityId(co.getGoogleEntityId());
        congress.setFecCandidateId(co.getFecCandidateId());
        congress.setUrl(co.getUrl());
        congress.setRssUrl(co.getRssUrl());
        congress.setContactForm(co.getContactForm());
        congress.setInOffice(co.getInOffice());
        congress.setDwNominate(co.getDwNominate());
        congress.setSeniority(co.getSeniority());
        congress.setNextElection(co.getNextElection());
        congress.setTotalVotes(co.getTotalVotes());
        congress.setMissedVotes(co.getMissedVotes());
        congress.setTotalPresent(co.getTotalPresent());

        LocalDateTime lastUpdate = DateTimeConversion.formatStringForLocalDateTime(co.getLastUpdated());
        congress.setLastUpdated(lastUpdate);

        congress.setOcdId(co.getOcdId());
        congress.setOffice(co.getOffice());
        congress.setPhone(co.getPhone());
        congress.setState(co.getState());
        congress.setDistrict(co.getDistrict());
        congress.setAtLarge(co.getAtLarge());
        congress.setGeoid(co.getGeoid());
        congress.setMissedVotesPct(co.getMissedVotesPct());
        congress.setVotesWithPartyPct(co.getVotesWithPartyPct());
        congress.setVotesAgainstPartyPct(co.getVotesAgainstPartyPct());
        return congress;
    }

    private Congress createCongress(ProPublicaCongress proPublicaCongress) {
        var newCongress = new Congress();
        var bp = setCongress(newCongress, proPublicaCongress);
        return congressRepo.save(bp);
    }

    private Optional<Congress> getCongressByPpId(String proPublicaId) {
        return congressRepo.findFirstCongressByProPublicaId(proPublicaId);
    }
    private Congress updateCongress(ProPublicaCongress proPublicaCongress) {
        var possibleCongress = getCongressByPpId(proPublicaCongress.getId());
        if (possibleCongress.isPresent()) {
            var congress = possibleCongress.get();
            var bp = setCongress(congress, proPublicaCongress);
            return congressRepo.save(bp);
        } else {
            logger.error("No Blog Post with ID {}", proPublicaCongress.getId());
            return null;
        }

    }

    public List<Congress> mapPropublicaResponseToCongress(List<ProPublicaCongress> congress) {
        var congressPPIds = getCongress().parallelStream()
                .map(Congress::getProPublicaId)
                .collect(Collectors.toList());

        var newCongresss = congress.stream()
                .filter(p -> !congressPPIds.contains(p.getId()))
                .collect(Collectors.toList());

        var updatePosts = congress.stream()
                .filter(p -> congressPPIds.contains(p.getId()))
                .collect(Collectors.toList());

        var mergedList = new ArrayList<Congress>();
        var createList = newCongresss.parallelStream()
                .map(s -> createCongress(s))
                .collect(Collectors.toList());
        var updateList = updatePosts.parallelStream()
                .map(s -> updateCongress(s))
                .collect(Collectors.toList());

        if (!createList.isEmpty()) mergedList.addAll(createList);
        if (!updateList.isEmpty()) mergedList.addAll(updateList);

        return mergedList;

    }
}
