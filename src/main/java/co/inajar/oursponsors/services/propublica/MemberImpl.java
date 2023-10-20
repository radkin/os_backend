package co.inajar.oursponsors.services.propublica;

import co.inajar.oursponsors.dbos.entities.User;
import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.dbos.entities.user.Preferences;
import co.inajar.oursponsors.dbos.repos.propublica.CongressRepo;
import co.inajar.oursponsors.dbos.repos.propublica.SenatorRepo;
import co.inajar.oursponsors.models.opensecrets.contributor.SmallContributorResponse;
import co.inajar.oursponsors.models.opensecrets.sector.SmallSectorResponse;
import co.inajar.oursponsors.models.propublica.congress.CongressDetailsResponse;
import co.inajar.oursponsors.models.propublica.congress.CongressResponse;
import co.inajar.oursponsors.models.propublica.senator.SenatorDetailsResponse;
import co.inajar.oursponsors.models.propublica.senator.SenatorResponse;
import co.inajar.oursponsors.models.user.PreferencesResponse;
import co.inajar.oursponsors.services.opensecrets.CandidateManager;
import co.inajar.oursponsors.services.preferences.PreferenceManager;
import co.inajar.oursponsors.services.user.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberImpl implements MemberManager {

    @Autowired
    private SenatorRepo senatorRepo;
    @Autowired
    private CongressRepo congressRepo;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CandidateManager candidateManager;

    @Autowired
    private PreferenceManager preferenceManager;

    private Logger logger = LoggerFactory.getLogger(MemberApiImpl.class);

    @Override
    public Optional<List<Senator>> getSenators(User user) {
        var preferences = preferenceManager.getPreferencesByUserId(user.getId());
        if (Boolean.TRUE.equals(preferences.getMyStateOnly()) && Boolean.TRUE.equals(!preferences.getMyPartyOnly()))
            return senatorRepo.findSenatorsByState(user.getState());
        if (Boolean.TRUE.equals(preferences.getMyPartyOnly()) && Boolean.TRUE.equals(!preferences.getMyStateOnly()))
            return senatorRepo.findSenatorsByParty(user.getParty());
        if (preferences.getMyStateOnly() && preferences.getMyPartyOnly())
            return senatorRepo.findSenatorsByStateAndParty(user.getState(), user.getParty());
        return Optional.of(senatorRepo.findAll());
    }

    @Override
    public Optional<Senator> getSenatorById(Long id) {
        return senatorRepo.findSenatorById(id);
    }

    @Override
    public Optional<List<Congress>> getCongress(User user) {
        var preferences = preferenceManager.getPreferencesByUserId(user.getId());
        if (Boolean.TRUE.equals(preferences.getMyStateOnly()) && Boolean.TRUE.equals(!preferences.getMyPartyOnly()))
            return congressRepo.findCongressesByState(user.getState());
        if (Boolean.TRUE.equals(preferences.getMyPartyOnly()) && Boolean.TRUE.equals(!preferences.getMyStateOnly()))
            return congressRepo.findCongressesByParty(user.getParty());
        if (preferences.getMyStateOnly() && preferences.getMyPartyOnly())
            return congressRepo.findCongressesByStateAndParty(user.getState(), user.getParty());
        return Optional.of(congressRepo.findAll());
    }

    @Override
    public Optional<Congress> getCongressById(Long id) {
        return congressRepo.findCongressById(id);
    }

    @Override
    public Optional<SenatorDetailsResponse> gatherSenatorDetailsResponse(Senator senator, Preferences preferences) {
        var senatorDetails = new SenatorDetailsResponse();

        // Senator
        var senatorResponse = new SenatorResponse(senator);
        senatorDetails.setSenator(senatorResponse);
        // Preferences
        var preferencesResponse = new PreferencesResponse(preferences);
        senatorDetails.setPreferences(preferencesResponse);
        // sectors
        var possibleSectors = candidateManager.getSectorsByCid(senator.getCrpId());
        if (possibleSectors.isPresent() && !possibleSectors.isEmpty() && possibleSectors.get().size() != 0) {
            var list = possibleSectors.get().parallelStream()
                    .map(SmallSectorResponse::new)
                    .toList();
            senatorDetails.setSectors(list);
        }
        // contributors
        var possibleContributors = candidateManager.getContributorsByCid(senator.getCrpId());
        if (possibleContributors.isPresent() && !possibleContributors.isEmpty() && possibleContributors.get().size() != 0) {
            var list = possibleContributors.get().parallelStream()
                    .map(SmallContributorResponse::new)
                    .toList();
            senatorDetails.setContributors(list);
        }
        return Optional.of(senatorDetails);

    }

    @Override
    public Optional<CongressDetailsResponse> gatherCongressDetailsResponse(Congress congress, Preferences preferences) {
        var congressDetails = new CongressDetailsResponse();

        // Senator
        var congressResponse = new CongressResponse(congress);
        congressDetails.setCongress(congressResponse);
        // Preferences
        var preferencesResponse = new PreferencesResponse(preferences);
        congressDetails.setPreferences(preferencesResponse);
        // sectors
        var possibleSectors = candidateManager.getSectorsByCid(congress.getCrpId());
        if (possibleSectors.isPresent() && !possibleSectors.isEmpty() && possibleSectors.get().size() != 0) {
            var list = possibleSectors.get().parallelStream()
                    .map(SmallSectorResponse::new)
                    .toList();
            congressDetails.setSectors(list);
        }
        // contributors
        var possibleContributors = candidateManager.getContributorsByCid(congress.getCrpId());
        if (possibleContributors.isPresent() && !possibleContributors.isEmpty() && possibleContributors.get().size() != 0) {
            var list = possibleContributors.get().parallelStream()
                    .map(SmallContributorResponse::new)
                    .toList();
            congressDetails.setContributors(list);
        }
        return Optional.of(congressDetails);

    }

}
