package co.inajar.oursponsors.services.propublica;

import co.inajar.oursponsors.dbOs.entities.chambers.Congress;
import co.inajar.oursponsors.dbOs.entities.chambers.Senator;

import java.util.List;

public interface MembersManager {

    List<Senator> getSenators();
    List<Congress> getCongress();

}
