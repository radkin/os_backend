package co.inajar.oursponsors.services.fec;

import co.inajar.oursponsors.dbos.entities.campaigns.Committee;
import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.models.fec.FecCommitteeDonor;
import co.inajar.oursponsors.models.opensecrets.CampaignResponse;

import java.util.List;

public interface CommitteeManager {

    List<FecCommitteeDonor> getFecCommitteeDonors(String committeeId, Integer twoYearTransactionPeriod);

    CampaignResponse getSenatorCampaignListResponse(Senator senator);

    CampaignResponse getCongressCampaignListResponse(Congress congress);

    List<Committee> getCommittees();
    Committee createCommittee(Object entity, String cmte);
}
