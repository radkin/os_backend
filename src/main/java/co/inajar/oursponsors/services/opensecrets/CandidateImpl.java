package co.inajar.oursponsors.services.opensecrets;

import co.inajar.oursponsors.dbos.entities.candidates.Contributor;
import co.inajar.oursponsors.dbos.entities.candidates.Sector;
import co.inajar.oursponsors.dbos.repos.opensecrets.ContributorRepo;
import co.inajar.oursponsors.dbos.repos.opensecrets.SectorRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidateImpl implements CandidateManager {

    @Autowired
    private SectorRepo sectorRepo;

    @Autowired
    private ContributorRepo contributorRepo;

    private Logger logger = LoggerFactory.getLogger(CandidateImpl.class);

    @Override
    public Optional<List<Sector>> getSectorsByCid(String cid) {
        return sectorRepo.findTop10SectorsByCidOrderByTotalDesc(cid);
    }

    @Override
    public Optional<List<Contributor>> getContributorsByCid(String cid) {
        return contributorRepo.findTop10ContributorsByCidOrderByTotalDesc(cid);
    }

}
