package co.inajar.oursponsors.services.opensecrets;


import co.inajar.oursponsors.dbos.entities.candidates.Contributor;
import co.inajar.oursponsors.dbos.entities.candidates.Sector;

import java.util.List;
import java.util.Optional;

public interface CandidateManager {

    Optional<List<Sector>> getSectorsByCid(String cid);

    Optional<List<Contributor>> getContributorsByCid(String cid);
}
