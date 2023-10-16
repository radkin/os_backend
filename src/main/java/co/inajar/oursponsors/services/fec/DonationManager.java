package co.inajar.oursponsors.services.fec;

import co.inajar.oursponsors.dbos.entities.campaigns.Donation;
import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.models.fec.FecCommitteeDonor;

public interface DonationManager {

    Donation mapFecDonorToDonation(FecCommitteeDonor donor, Sponsor sponsor, String ppId);
}
