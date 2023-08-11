package co.inajar.oursponsors.dbos.repos.fec;

import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface SponsorsRepo extends JpaRepository<Sponsor, Long> {

    Sponsor findByContributorName(String name);

    List<Sponsor> findSponsorsBySenatorId(Long senatorId);

    List<Sponsor> findSponsorsByCongressId(Long congressId);


    //    List<Sponsor> findSponsorsBySenatorId(Long senatorId);
//    @Query(value = "SELECT s FROM Sponsor AS s " +
//            "WHERE s.senator.id=:senatorId")
//    List<Sponsor> findSponsorsBySenatorId(Long senatorId);


}
