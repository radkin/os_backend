package co.inajar.oursponsors.dbos.repos;

import co.inajar.oursponsors.dbos.entities.campaigns.Committee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface CommitteeRepo extends JpaRepository<Committee, Long> {

}
