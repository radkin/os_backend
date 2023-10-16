package co.inajar.oursponsors.services.fec;

import co.inajar.oursponsors.dbos.entities.SponsorCongress;
import co.inajar.oursponsors.dbos.entities.SponsorSenator;
import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.dbos.repos.SponsorCongressRepo;
import co.inajar.oursponsors.dbos.repos.SponsorSenatorsRepo;
import co.inajar.oursponsors.dbos.repos.fec.SponsorsRepo;
import co.inajar.oursponsors.dbos.repos.propublica.CongressRepo;
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

@Service
public class SponsorsImpl implements SponsorsManager {

    @Autowired
    SponsorsRepo sponsorsRepo;

    @Autowired
    SenatorRepo senatorRepo;
    @Autowired
    CongressRepo congressRepo;

    @Autowired
    SponsorSenatorsRepo sponsorSenatorsRepo;
    @Autowired
    SponsorCongressRepo sponsorCongressRepo;

    private Logger logger = LoggerFactory.getLogger(SponsorsImpl.class);

    @Override
    public List<Sponsor> getSponsors(SponsorRequest data) {
        if (data.getChamber().equals("senator")) {
            Optional<Senator> senator = senatorRepo.findById(data.getOsId());
            if (senator.isPresent()) {
                Optional<List<SponsorSenator>> possibleSponsorSenators = sponsorSenatorsRepo.findSponsorsBySenatorId(senator.get().getId());
                if (possibleSponsorSenators.isPresent()) {
                    var sponsorSenators = possibleSponsorSenators.get().parallelStream()
                            .map(SponsorSenator::getSponsor)
                            .toList();
                    List<Long> sponsorIds = sponsorSenators.parallelStream()
                            .map(Sponsor::getId)
                            .toList();
                    var sponsors = sponsorIds.parallelStream()
                            .map(this::getSponsorById)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .toList();
                    return sponsors.stream()
                            .sorted(Comparator.comparing(Sponsor::getContributorAggregateYtd).reversed())
                            .toList();
                }
            }
        } else if (data.getChamber().equals("congress")) {
            Optional<Congress> congress = congressRepo.findById(data.getOsId());
            if (congress.isPresent()) {
                Optional<List<SponsorCongress>> possibleSponsorCongress = sponsorCongressRepo.findSponsorsByCongressId(congress.get().getId());
                if (possibleSponsorCongress.isPresent()) {
                    var sponsorCongress = possibleSponsorCongress.get().parallelStream()
                            .map(SponsorCongress::getSponsor)
                            .toList();
                    List<Long> sponsorIds = sponsorCongress.parallelStream()
                            .map(Sponsor::getId)
                            .toList();
                    var sponsors = sponsorIds.parallelStream()
                            .map(this::getSponsorById)
                            .filter(Optional::isPresent)
                            .map(Optional::get)
                            .toList();
                    return sponsors.stream()
                            .sorted(Comparator.comparing(Sponsor::getContributorAggregateYtd).reversed())
                            .toList();
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
