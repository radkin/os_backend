package co.inajar.oursponsors.dbOs.repos.opensecrets;

import co.inajar.oursponsors.dbOs.entities.candidates.Sector;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface SectorRepo extends JpaRepository<Sector, Long> {

    Optional<Sector> findSectorByCidAndCycleAndSectorName(String cid, Year cycle, String sectorName);
}