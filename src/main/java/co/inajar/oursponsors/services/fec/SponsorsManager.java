package co.inajar.oursponsors.services.fec;

import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.models.fec.SponsorRequest;

import java.util.List;

public interface SponsorsManager {

    List<Sponsor> getSponsors(SponsorRequest data);
}
