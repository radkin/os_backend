package co.inajar.oursponsors.services.fec;

import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.models.fec.SponsorRequest;

import java.util.Set;

public interface CommitteesManager {

    Set<Sponsor> getSponsors(SponsorRequest data);
}
