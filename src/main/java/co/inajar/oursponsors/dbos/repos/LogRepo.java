package co.inajar.oursponsors.dbos.repos;

import co.inajar.oursponsors.dbos.entities.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LogRepo extends JpaRepository<Log, UUID> {

}
