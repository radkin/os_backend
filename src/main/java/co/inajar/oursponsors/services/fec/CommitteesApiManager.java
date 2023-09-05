package co.inajar.oursponsors.services.fec;

import co.inajar.oursponsors.models.fec.FecCommitteeDonor;
import co.inajar.oursponsors.models.opensecrets.CampaignResponse;
import co.inajar.oursponsors.models.opensecrets.CommitteeRequest;

import java.util.List;

public interface CommitteesApiManager {

    List<FecCommitteeDonor> getFecCommitteeDonors(String committeeId, Integer twoYearTransactionPeriod);

    CampaignResponse getCampaignListResponse(CommitteeRequest data);

}
