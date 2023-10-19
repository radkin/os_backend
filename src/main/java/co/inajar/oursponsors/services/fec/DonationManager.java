package co.inajar.oursponsors.services.fec;

import co.inajar.oursponsors.dbos.entities.campaigns.Donation;
import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.models.fec.FecCommitteeDonor;
import co.inajar.oursponsors.models.fec.MiniDonationResponse;

import java.util.List;

public interface DonationManager {

    Donation mapFecDonorToDonation(FecCommitteeDonor donor, Sponsor sponsor, String ppId);
    List<MiniDonationResponse> mapDonationResponses(List<Sponsor> sponsors);
}
