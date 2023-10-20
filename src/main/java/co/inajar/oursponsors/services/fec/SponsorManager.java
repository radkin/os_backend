package co.inajar.oursponsors.services.fec;

import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.models.fec.FecCommitteeDonor;
import co.inajar.oursponsors.models.fec.SponsorRequest;

import java.util.List;
import java.util.Map;

public interface SponsorManager {

    List<Sponsor> getSponsors(SponsorRequest data);
    List<Sponsor> processDonorsAndGetNewSponsors(String chamber, Long osId, String proPublicaId, Map<String, List<FecCommitteeDonor>> cmteFecDonors);
}
