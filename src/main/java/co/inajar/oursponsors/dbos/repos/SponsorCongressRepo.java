package co.inajar.oursponsors.dbos.repos;

import co.inajar.oursponsors.dbos.entities.SponsorCongress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface SponsorCongressRepo extends JpaRepository<SponsorCongress, Long> {

    @Query(value = "SELECT sp FROM SponsorCongress AS sp " +
            "WHERE sp.congress.id=:congressId")
    Optional<List<SponsorCongress>> findSponsorsByCongressId(Long congressId);
}
