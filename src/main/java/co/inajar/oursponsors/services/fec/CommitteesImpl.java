package co.inajar.oursponsors.services.fec;

import co.inajar.oursponsors.dbos.entities.SponsorSenator;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<Sponsor> getSponsors(SponsorRequest data) {
        if (data.getChamber().equals("senator")) {
            Optional<Senator> senator = senatorRepo.findById(data.getId());
            if (senator.isPresent()) {
                Optional<List<SponsorSenator>> possibleSponsorSenators = sponsorSenatorsRepo.findSponsorsBySenatorId(senator.get().getId());
                if (possibleSponsorSenators.isPresent()) {
                    var sponsorSenators = possibleSponsorSenators.get().parallelStream()
                            .map(SponsorSenator::getSponsor)
                            .collect(Collectors.toList());
                    List<Long> sponsorIds = sponsorSenators.parallelStream()
                            .map(Sponsor::getId)
                            .collect(Collectors.toList());
                    var sponsors = sponsorIds.parallelStream()
                            .map(this::getSponsorById)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .collect(Collectors.toList());
                    return sponsors.stream()
                            .sorted(Comparator.comparing(Sponsor::getContributorAggregateYtd).reversed())
                            .collect(Collectors.toList());
                }
            }
        } else {
            logger.error("Unable to process request. Chamber and ID are required");
        }
        return Collections.emptyList();
    }

    private Optional<Sponsor> getSponsorById(Long id) {
        return sponsorsRepo.findById(id);
    }
}
