package co.inajar.oursponsors.services.fec;

import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.dbos.repos.SponsorSenatorsRepo;
import co.inajar.oursponsors.dbos.repos.fec.SponsorsRepo;
import co.inajar.oursponsors.dbos.repos.propublica.SenatorRepo;
import co.inajar.oursponsors.models.fec.SponsorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class CommitteesImpl implements CommitteesManager {

    @Autowired
    SponsorsRepo sponsorsRepo;

    @Autowired
    SenatorRepo senatorRepo;

    @Autowired
    SponsorSenatorsRepo sponsorSenatorsRepo;

    private Logger logger = LoggerFactory.getLogger(CommitteesImpl.class);

    @Override
    public Set<Sponsor> getSponsors(SponsorRequest data) {
        if (data.getChamber().equals("senator")) {
            Senator senator = senatorRepo.findById(data.getId()).orElse(null);
            if (senator != null) {
                return senator.getSponsors();
            }

        } else {
            logger.error("Unable to process request. Chamber and ID are required");
        }
        return Collections.emptySet();
    }
}
