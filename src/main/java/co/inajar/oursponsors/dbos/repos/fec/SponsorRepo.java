package co.inajar.oursponsors.dbos.repos.fec;

import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface SponsorRepo extends JpaRepository<Sponsor, Long> {

    Sponsor findByContributorName(String name);

}
