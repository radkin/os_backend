package co.inajar.oursponsors.dbOs.repos.opensecrets;

import co.inajar.oursponsors.dbOs.entities.candidates.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface SectorRepo extends JpaRepository<Sector, Long> {

}
