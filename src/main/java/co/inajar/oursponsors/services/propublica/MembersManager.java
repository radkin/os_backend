package co.inajar.oursponsors.services.propublica;

import co.inajar.oursponsors.dbOs.entities.chambers.Congress;
import co.inajar.oursponsors.dbOs.entities.chambers.Senator;

import java.util.List;
import java.util.Optional;

public interface MembersManager {

    List<Senator> getSenators();
    Optional<List<Senator>> getSenatorsByState(String state);
    List<Congress> getCongress();
    Optional<List<Congress>> getCongressByState(String state);

}
