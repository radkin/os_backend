package co.inajar.oursponsors.services.opensecrets;

import co.inajar.oursponsors.dbOs.entities.candidates.Sector;
import co.inajar.oursponsors.dbOs.repos.opensecrets.SectorRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CandidatesImpl implements CandidatesManager {

    @Autowired
    private SectorRepo sectorRepo;

    private Logger logger = LoggerFactory.getLogger(CandidatesImpl.class);

    @Override
    public Optional<List<Sector>> getSectorsByCid(String cid) {
        return sectorRepo.findSectorsByCid(cid);
    }


}
