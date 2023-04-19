package co.inajar.oursponsors.dbOs.repos.propublica;

import co.inajar.oursponsors.dbOs.entities.chambers.Senator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface SenatorRepo extends JpaRepository<Senator, Long> {

    Optional<Senator> findFirstSenatorByProPublicaId(String proPublicaId);
    Optional<List<Senator>> findSenatorsByState(String state);
    Optional<List<Senator>> findSenatorsByParty(String party);
    Optional<List<Senator>> findSenatorsByStateAndParty(String state, String party);
}

