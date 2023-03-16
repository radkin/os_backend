package co.inajar.oursponsors.services.propublica;

import co.inajar.oursponsors.dbOs.entities.chambers.Congress;
import co.inajar.oursponsors.dbOs.entities.chambers.Senator;
import co.inajar.oursponsors.models.propublica.ProPublicaCongress;
import co.inajar.oursponsors.models.propublica.ProPublicaSenator;

import java.util.List;

public interface MembersApiManager {

    List<ProPublicaSenator> getSenatorsListResponse();
    List<Senator> mapPropublicaResponseToSenators(List<ProPublicaSenator> senators);
    List<ProPublicaCongress> getCongressListResponse();
    List<Congress> mapPropublicaResponseToCongress(List<ProPublicaCongress> congress);
}
