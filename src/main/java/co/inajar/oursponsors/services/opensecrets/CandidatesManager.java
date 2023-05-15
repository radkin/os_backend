package co.inajar.oursponsors.services.opensecrets;


import co.inajar.oursponsors.dbOs.entities.candidates.Contributor;
import co.inajar.oursponsors.dbOs.entities.candidates.Sector;

import java.util.List;
import java.util.Optional;

public interface CandidatesManager {

    Optional<List<Sector>> getSectorsByCid(String cid);
    Optional<List<Contributor>> getContributorsByCid(String cid);
}
