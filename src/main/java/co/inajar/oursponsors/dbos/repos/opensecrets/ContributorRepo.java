package co.inajar.oursponsors.dbos.repos.opensecrets;

import co.inajar.oursponsors.dbos.entities.candidates.Contributor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface ContributorRepo extends JpaRepository<Contributor, Long> {

    Optional<List<Contributor>> findTop10ContributorsByCidOrderByTotalDesc(String cid);
}
