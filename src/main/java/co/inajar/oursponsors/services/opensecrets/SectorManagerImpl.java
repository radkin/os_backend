package co.inajar.oursponsors.services.opensecrets;

import co.inajar.oursponsors.dbos.entities.candidates.Sector;
import co.inajar.oursponsors.dbos.repos.opensecrets.SectorRepo;
import co.inajar.oursponsors.models.opensecrets.sector.OpenSecretsSector;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SectorManagerImpl implements SectorManager {

    SectorRepo sectorRepo;

    public Sector createSector(OpenSecretsSector openSecretsSector) {
        var newSector = new Sector();
        var ns = setSector(newSector, openSecretsSector);
        return sectorRepo.save(ns);
    }

    private Sector setSector(Sector sector, OpenSecretsSector oss) {
        sector.setCid(oss.getCid());
        sector.setCycle(oss.getCycle());
        sector.setSectorName(oss.getSectorName());
        sector.setSectorId(oss.getSectorId());
        sector.setIndivs(Integer.valueOf(oss.getIndivs()));
        sector.setPacs(Integer.valueOf(oss.getPacs()));
        sector.setTotal(Integer.valueOf(oss.getTotal()));
        return sector;
    }

    @Override
    public List<Sector> mapOpenSecretsResponseToSectors(List<OpenSecretsSector> sectors) {
        // for now every sector is a new one. We are not set up for updates. Delete all prior to refresh
        return sectors.parallelStream()
                .map(this::createSector)
                .toList();
    }

    @Override
    public Optional<List<Sector>> getSectorsByCid(String cid) {
        return sectorRepo.findTop10SectorsByCidOrderByTotalDesc(cid);
    }
}
