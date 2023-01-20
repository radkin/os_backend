package co.inajar.oursponsors.services.propublica;

import co.inajar.oursponsors.dbOs.entities.chamber.senate.Senator;
import co.inajar.oursponsors.dbOs.repos.propublica.SenatorRepo;
import co.inajar.oursponsors.models.propublica.ProPublicaSenator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class MembersApiImpl implements MembersApiManager {

    @Autowired
    private SenatorRepo senatorRepo;
    
    private Logger logger = LoggerFactory.getLogger((MembersApiImpl.class));

    @Value("${propublica.inajar.token.secret}")
    private String propublicaApiKey;

    private List<Senator> getSenators() { return senatorRepo.findAll(); }

    @Override
    public List<ProPublicaSenator> getSenatorsListResponse() {
//        Integer page = 1;
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
//        senator.setDateOfBirth(sr.getDateOfBirth());
        senator.setGender(sr.getGender());
        senator.setParty(sr.getParty());
        senator.setLeadershipRole(sr.getLeadershipRole());
        senator.setTwitterAccount(sr.getTwitterAccount());
        senator.setFacebookAccount(sr.getFacebookAccount());
        senator.setYoutubeAccount(sr.getYoutubeAccount());
        senator.setGovtrackId(sr.getGovtrackId());
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
//        senator.setNextElection(sr.getNextElection());
        senator.setTotalVotes(sr.getTotalVotes());
        senator.setMissedVotes(sr.getMissedVotes());
        senator.setTotalPresent(sr.getTotalPresent());
//        senator.setLastUpdated(sr.getLastUpdated());
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

}
