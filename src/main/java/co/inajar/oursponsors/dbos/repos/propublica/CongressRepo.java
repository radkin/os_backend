package co.inajar.oursponsors.dbos.repos.propublica;

import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface CongressRepo extends JpaRepository<Congress, Long> {
    Optional<Congress> findFirstCongressByProPublicaId(String proPublicaId);

    Optional<List<Congress>> findCongressesByState(String state);

    Optional<List<Congress>> findCongressesByParty(String party);

    Optional<List<Congress>> findCongressesByStateAndParty(String state, String party);

    Optional<Congress> findCongressById(Long id);
}

