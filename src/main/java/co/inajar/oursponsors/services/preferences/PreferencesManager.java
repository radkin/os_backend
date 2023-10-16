package co.inajar.oursponsors.services.preferences;

import co.inajar.oursponsors.dbos.entities.Preferences;
import co.inajar.oursponsors.dbos.entities.User;
import co.inajar.oursponsors.models.user.PreferencesRequest;

// Note: the entity is called Preferences so this service is also plural
public interface PreferencesManager {

    Preferences getPreferencesByUserId(Long id);

    Preferences updateUserPreferences(PreferencesRequest data, Long id);

    void createUserPreferences(User user);
}
