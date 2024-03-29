package co.inajar.oursponsors.services.propublica;

import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.models.propublica.congress.ProPublicaCongress;
import co.inajar.oursponsors.models.propublica.senator.ProPublicaSenator;

import java.util.List;

public interface MemberApiManager {

    List<ProPublicaSenator> getSenatorsListResponse();

    List<Senator> mapPropublicaResponseToSenators(List<ProPublicaSenator> senators);

    List<ProPublicaCongress> getCongressListResponse();

    List<Congress> mapPropublicaResponseToCongress(List<ProPublicaCongress> congress);
}
