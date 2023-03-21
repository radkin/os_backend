package co.inajar.oursponsors.services.propublica;

import co.inajar.oursponsors.dbOs.entities.chambers.Congress;
import co.inajar.oursponsors.dbOs.entities.chambers.Senator;
import co.inajar.oursponsors.dbOs.repos.propublica.CongressRepo;
import co.inajar.oursponsors.dbOs.repos.propublica.SenatorRepo;
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

    private Logger logger = LoggerFactory.getLogger(MembersApiImpl.class);

    public List<Senator> getSenators() { return senatorRepo.findAll(); }
    public Optional<List<Senator>> getSenatorsByState(String state) { return senatorRepo.findSenatorsByState(state); }

    public List<Congress> getCongress() { return congressRepo.findAll(); }
    public Optional<List<Congress>> getCongressByState(String state) { return congressRepo.findCongressesByState(state); }

}
