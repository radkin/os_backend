package co.inajar.oursponsors.services.propublica;

import co.inajar.oursponsors.dbOs.entities.chamber.senate.Senator;

import java.util.List;

public interface MembersApiManager {

    List<Senator> getSenatorsListResponse();
    List<Senator> mapPropublicaResponseToSenators(List<Senator> senators);
}
