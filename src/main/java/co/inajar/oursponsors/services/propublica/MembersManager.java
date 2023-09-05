package co.inajar.oursponsors.services.propublica;

import co.inajar.oursponsors.dbos.entities.User;
import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;

import java.util.List;
import java.util.Optional;

public interface MembersManager {

    Optional<List<Senator>> getSenators(User user);

    Optional<Senator> getSenatorById(Long id);

    Optional<List<Congress>> getCongress(User user);

    Optional<Congress> getCongressById(Long id);


}
