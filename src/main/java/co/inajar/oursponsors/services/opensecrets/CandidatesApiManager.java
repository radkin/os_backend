package co.inajar.oursponsors.services.opensecrets;

import co.inajar.oursponsors.dbOs.entities.candidates.Sector;
import co.inajar.oursponsors.models.opensecrets.sector.OpenSecretsSector;
import co.inajar.oursponsors.models.opensecrets.sector.OpenSecretsSector;

import java.util.List;

public interface CandidatesApiManager {

//    List<Sector> getSectorsListResponse();
    List<OpenSecretsSector> getSectorsListResponse(Integer part);

    List<Sector> mapOpenSecretsResponseToSectors(List<OpenSecretsSector> sectors);
}
