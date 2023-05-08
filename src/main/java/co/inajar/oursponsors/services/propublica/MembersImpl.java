package co.inajar.oursponsors.services.propublica;

import co.inajar.oursponsors.dbOs.entities.chambers.Congress;
import co.inajar.oursponsors.dbOs.entities.chambers.Senator;
import co.inajar.oursponsors.dbOs.repos.propublica.CongressRepo;
import co.inajar.oursponsors.dbOs.repos.propublica.SenatorRepo;
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
    public Optional<List<Senator>> getSenators() {
        var preferences = userManager.getPreferencesByUserId(1L);
        if (preferences.getMyStateOnly() && !preferences.getMyPartyOnly()) return senatorRepo.findSenatorsByState("OH");
        if (preferences.getMyPartyOnly() && !preferences.getMyStateOnly()) return senatorRepo.findSenatorsByParty("D");
        if (preferences.getMyStateOnly() && preferences.getMyPartyOnly()) return senatorRepo.findSenatorsByStateAndParty("OH", "D");
        return Optional.of(senatorRepo.findAll());
    }
    @Override
    public Optional<List<Congress>> getCongress() {
        var preferences = userManager.getPreferencesByUserId(1L);
        if (preferences.getMyStateOnly() && !preferences.getMyPartyOnly()) return congressRepo.findCongressesByState("CO");
        if (preferences.getMyPartyOnly() && !preferences.getMyStateOnly()) return congressRepo.findCongressesByParty("R");
        if (preferences.getMyStateOnly() && preferences.getMyPartyOnly()) return congressRepo.findCongressesByStateAndParty("CO", "R");
        return Optional.of(congressRepo.findAll());
    }

}
