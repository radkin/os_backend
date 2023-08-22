package co.inajar.oursponsors.dbos.repos;

import co.inajar.oursponsors.dbos.entities.SponsorSenator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface SponsorSenatorsRepo extends JpaRepository<SponsorSenator, Long> {

    @Query(value = "SELECT sp FROM SponsorSenator AS sp " +
            "WHERE sp.senator.id=:senatorId")
    Optional<List<SponsorSenator>> findSponsorsBySenatorId(Long senatorId);
}
