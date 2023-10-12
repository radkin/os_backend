package co.inajar.oursponsors.services.user;

import co.inajar.oursponsors.dbos.entities.User;
import co.inajar.oursponsors.dbos.entities.user.Preferences;
import co.inajar.oursponsors.models.user.PreferencesRequest;

public interface PreferencesManager {

    Preferences getPreferencesByUserId(Long id);

    Preferences updateUserPreferences(PreferencesRequest data, Long id);

    void createUserPreferences(User user);
}
