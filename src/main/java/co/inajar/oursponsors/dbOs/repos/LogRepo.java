package co.inajar.oursponsors.dbOs.repos;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.inajar.oursponsors.dbOs.entities.Log;

@Repository
public interface LogRepo extends JpaRepository<Log, UUID> {

}
