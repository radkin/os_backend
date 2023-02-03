package co.inajar.oursponsors.services.propublica;

import co.inajar.oursponsors.dbOs.entities.chamber.senate.Senator;
import co.inajar.oursponsors.dbOs.repos.propublica.SenatorRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MembersImpl implements MembersManager {

    @Autowired
    private SenatorRepo senatorRepo;

    private Logger logger = LoggerFactory.getLogger(MembersApiImpl.class);

    public List<Senator> getSenators() { return senatorRepo.findAll(); }

}
