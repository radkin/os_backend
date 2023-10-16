package co.inajar.oursponsors.services.fec;

import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.models.fec.FecCommitteeDonor;
import co.inajar.oursponsors.models.fec.SponsorRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SponsorManager {

    List<Sponsor> getSponsors(SponsorRequest data);

    Optional<Sponsor> getSponsorByName(String name);

    Sponsor mapFecDonorToSponsor(FecCommitteeDonor donor, Long osId, String chamber);

    List<Sponsor> processDonorsAndGetNewSponsors(String chamber, Long osId, String proPublicaId, Map<String, List<FecCommitteeDonor>> cmteFecDonors);
}
