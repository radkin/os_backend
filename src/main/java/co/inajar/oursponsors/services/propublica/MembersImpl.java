package co.inajar.oursponsors.services.propublica;

import co.inajar.oursponsors.dbos.entities.User;
import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.dbos.repos.propublica.CongressRepo;
import co.inajar.oursponsors.dbos.repos.propublica.SenatorRepo;
import co.inajar.oursponsors.services.user.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MembersImpl implements MembersManager {

    @Autowired
    private SenatorRepo senatorRepo;
    @Autowired
    private CongressRepo congressRepo;
    @Autowired
    private UserManager userManager;

    private Logger logger = LoggerFactory.getLogger(MembersApiImpl.class);

    @Override
    public Optional<List<Senator>> getSenators(User user) {
        var preferences = userManager.getPreferencesByUserId(user.getId());
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
        var preferences = userManager.getPreferencesByUserId(user.getId());
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
}
