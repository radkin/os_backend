package co.inajar.oursponsors.dbOs.repos;

import co.inajar.oursponsors.dbOs.entities.user.Preferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PreferencesRepo extends JpaRepository<Preferences, Long> {
    Preferences findPreferencesByUserId(Long id);

}
