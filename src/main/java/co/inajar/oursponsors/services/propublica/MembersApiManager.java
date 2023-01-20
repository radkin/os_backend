package co.inajar.oursponsors.services.propublica;

import co.inajar.oursponsors.dbOs.entities.chamber.senate.Senator;
import co.inajar.oursponsors.models.propublica.ProPublicaSenator;

import java.util.List;

public interface MembersApiManager {

    List<ProPublicaSenator> getSenatorsListResponse();
    List<Senator> mapPropublicaResponseToSenators(List<ProPublicaSenator> senators);
}
