package co.inajar.oursponsors.services.opensecrets;

import co.inajar.oursponsors.dbos.entities.candidates.Sector;
import co.inajar.oursponsors.models.opensecrets.sector.OpenSecretsSector;

import java.util.List;
import java.util.Optional;

public interface SectorManager {

    Sector createSector(OpenSecretsSector openSecretsSector);
    List<Sector> mapOpenSecretsResponseToSectors(List<OpenSecretsSector> sectors);
    Optional<List<Sector>> getSectorsByCid(String cid);
}
