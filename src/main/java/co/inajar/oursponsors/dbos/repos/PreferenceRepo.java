package co.inajar.oursponsors.dbos.repos;

import co.inajar.oursponsors.dbos.entities.user.Preferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferenceRepo extends JpaRepository<Preferences, Long> {
    Preferences findPreferencesByUserId(Long id);

}
