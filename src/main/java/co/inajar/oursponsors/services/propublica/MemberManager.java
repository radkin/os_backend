package co.inajar.oursponsors.services.propublica;

import co.inajar.oursponsors.dbos.entities.User;
import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.dbos.entities.user.Preferences;
import co.inajar.oursponsors.models.propublica.congress.CongressDetailsResponse;
import co.inajar.oursponsors.models.propublica.senator.SenatorDetailsResponse;

import java.util.List;
import java.util.Optional;

public interface MemberManager {

    Optional<List<Senator>> getSenators(User user);

    Optional<Senator> getSenatorById(Long id);

    Optional<List<Congress>> getCongress(User user);

    Optional<Congress> getCongressById(Long id);

    Optional<SenatorDetailsResponse> gatherSenatorDetailsResponse(Senator senator, Preferences preferences);

    Optional<CongressDetailsResponse> gatherCongressDetailsResponse(Congress congress, Preferences preferences);

}
