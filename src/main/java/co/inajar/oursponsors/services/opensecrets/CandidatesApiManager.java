package co.inajar.oursponsors.services.opensecrets;

import co.inajar.oursponsors.dbOs.entities.candidates.Sector;
import co.inajar.oursponsors.models.opensecrets.sector.CandSectorResponse;

import java.util.List;

public interface CandidatesApiManager {

//    List<Sector> getSectorsListResponse();
    List<String> getAllCandSectorsFromOpenSecrets();
}
