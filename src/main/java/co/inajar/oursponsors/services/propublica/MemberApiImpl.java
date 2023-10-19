package co.inajar.oursponsors.services.propublica;

import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.dbos.repos.propublica.CongressRepo;
import co.inajar.oursponsors.dbos.repos.propublica.SenatorRepo;
import co.inajar.oursponsors.helpers.DateTimeConversion;
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

@Service
public class MemberApiImpl implements MemberApiManager {

    @Autowired
    private SenatorRepo senatorRepo;

    @Autowired
    private CongressRepo congressRepo;

    @Value("${propublica.inajar.token.secret}")
    private String propublicaApiKey;

    private static final String BASE_API_URL = "https://api.propublica.org";

    private final Logger logger = LoggerFactory.getLogger(MemberApiImpl.class);

    // Senate
    @Override
    public List<ProPublicaCongress> getCongressListResponse() {
        String path = "congress/v1/117/house/members.json";
        return fetchAndMapProPublicaMembers(path, ProPublicaCongress.class);
    }

    @Override
    public List<ProPublicaSenator> getSenatorsListResponse() {
        String path = "congress/v1/117/senate/members.json";
        return fetchAndMapProPublicaMembers(path, ProPublicaSenator.class);
    }

    private <T> List<T> fetchAndMapProPublicaMembers(String path, Class<T> responseType) {
        WebClient webClient = createWebClient();

        String response = webClient.get()
                .uri(uriBuilder -> uriBuilder.path(path).build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return mapResponseToModel(response, responseType);
    }

    private <T> List<T> mapResponseToModel(String response, Class<T> responseType) {
        ObjectMapper objectMapper = new ObjectMapper();
        List<T> members = new ArrayList<>();
        try {
            var tree = objectMapper.readTree(response);
            var membersResponse = tree.get("results");
            for (JsonNode jsonNode : membersResponse) {
                T member = objectMapper.treeToValue(jsonNode, responseType);
                members.add(member);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return members;
    }


    private WebClient createWebClient() {
        return WebClient.builder()
                .exchangeStrategies(ExchangeStrategies.builder().codecs(
                                clientCodecConfigurer -> clientCodecConfigurer.defaultCodecs().maxInMemorySize(1000000))
                        .build())
                .baseUrl(BASE_API_URL)
                .defaultHeader("X-API-Key", propublicaApiKey)
                .build();
    }

    public List<Senator> mapPropublicaResponseToSenators(List<ProPublicaSenator> senators) {
        List<String> senatorPPIds = getSenators().stream()
                .map(Senator::getProPublicaId)
                .toList();

        List<ProPublicaSenator> newSenators = senators.stream()
                .filter(p -> !senatorPPIds.contains(p.getId()))
                .toList();

        List<ProPublicaSenator> updatePosts = senators.stream()
                .filter(p -> senatorPPIds.contains(p.getId()))
                .toList();

        List<Senator> mergedList = new ArrayList<>();
        List<Senator> createList = newSenators.parallelStream()
                .map(this::createSenator)
                .toList();
        List<Senator> updateList = updatePosts.parallelStream()
                .map(this::updateSenator)
                .toList();

        mergedList.addAll(createList);
        mergedList.addAll(updateList);

        return mergedList;
    }

    private List<Senator> getSenators() {
        return senatorRepo.findAll();
    }

    private Senator createSenator(ProPublicaSenator proPublicaSenator) {
        Senator newSenator = new Senator();
        setSenator(newSenator, proPublicaSenator);
        return senatorRepo.save(newSenator);
    }

    private Senator updateSenator(ProPublicaSenator proPublicaSenator) {
        Optional<Senator> possibleSenator = getSenatorByPpId(proPublicaSenator.getId());
        if (possibleSenator.isPresent()) {
            Senator senator = possibleSenator.get();
            setSenator(senator, proPublicaSenator);
            return senatorRepo.save(senator);
        } else {
            logger.error("No Senator with ID: {}", proPublicaSenator.getId());
            return null;
        }
    }

    private void setSenator(Senator senator, ProPublicaSenator sr) {
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
    }

    private Optional<Senator> getSenatorByPpId(String proPublicaId) {
        return senatorRepo.findFirstSenatorByProPublicaId(proPublicaId);
    }

    public List<Congress> mapPropublicaResponseToCongress(List<ProPublicaCongress> congress) {
        List<String> congressPPIds = getCongress().stream()
                .map(Congress::getProPublicaId)
                .toList();

        List<ProPublicaCongress> newCongresss = congress.stream()
                .filter(p -> !congressPPIds.contains(p.getId()))
                .toList();

        List<ProPublicaCongress> updatePosts = congress.stream()
                .filter(p -> congressPPIds.contains(p.getId()))
                .toList();

        List<Congress> mergedList = new ArrayList<>();
        List<Congress> createList = newCongresss.parallelStream()
                .map(this::createCongress)
                .toList();
        List<Congress> updateList = updatePosts.parallelStream()
                .map(this::updateCongress)
                .toList();

        mergedList.addAll(createList);
        mergedList.addAll(updateList);

        return mergedList;
    }

    private List<Congress> getCongress() {
        return congressRepo.findAll();
    }

    private Congress createCongress(ProPublicaCongress proPublicaCongress) {
        Congress newCongress = new Congress();
        setCongress(newCongress, proPublicaCongress);
        return congressRepo.save(newCongress);
    }

    private Congress updateCongress(ProPublicaCongress proPublicaCongress) {
        Optional<Congress> possibleCongress = getCongressByPpId(proPublicaCongress.getId());
        if (possibleCongress.isPresent()) {
            Congress congress = possibleCongress.get();
            setCongress(congress, proPublicaCongress);
            return congressRepo.save(congress);
        } else {
            logger.error("No Congress with ID: {}", proPublicaCongress.getId());
            return null;
        }
    }

    private void setCongress(Congress congress, ProPublicaCongress co) {
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
    }

    private Optional<Congress> getCongressByPpId(String proPublicaId) {
        return congressRepo.findFirstCongressByProPublicaId(proPublicaId);
    }
}
