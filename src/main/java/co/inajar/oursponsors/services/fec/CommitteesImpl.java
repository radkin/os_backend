package co.inajar.oursponsors.services.fec;

import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.dbos.repos.fec.SponsorsRepo;
import co.inajar.oursponsors.models.fec.SponsorRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommitteesImpl implements CommitteesManager {

    @Autowired
    SponsorsRepo sponsorsRepo;

    private Logger logger = LoggerFactory.getLogger(CommitteesImpl.class);

    @Override
    public List<Sponsor> getSponsors(SponsorRequest data) {
        var sponsors = new ArrayList<Sponsor>();
        Optional<List<Sponsor>> possibleSponsors = Optional.of(new ArrayList<Sponsor>());
        if (data.getChamber().equals("senator")) {
            possibleSponsors = Optional.ofNullable(sponsorsRepo.findSponsorsBySenatorId(data.getId()));
        } else if (data.getChamber().equals("congress")) {
            System.out.println("nothing to see here");
            possibleSponsors = Optional.ofNullable(sponsorsRepo.findSponsorsByCongressId(data.getId()));
        } else {
            logger.error("Unable to process request. Chamber and ID are required");
        }
        if (possibleSponsors.isPresent()) {
            sponsors.addAll(possibleSponsors.get());
        }

        return sponsors;
    }
}
