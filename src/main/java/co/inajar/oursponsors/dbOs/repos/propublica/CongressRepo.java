package co.inajar.oursponsors.dbOs.repos.propublica;

import co.inajar.oursponsors.dbOs.entities.chambers.Congress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CongressRepo extends JpaRepository<Congress, Long> {

    Optional<Congress> findFirstCongressByProPublicaId(String proPublicaId);
}

