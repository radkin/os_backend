package co.inajar.oursponsors.services.user;

import co.inajar.oursponsors.dbOs.entities.User;
import co.inajar.oursponsors.dbOs.entities.user.Preferences;

import java.util.Optional;

public interface UserManager {
    Optional<User> getUserById(Long id);
    Optional<User> getUserByApiKey(String apiKey);
    Preferences getPreferencesByUserId(Long id);
}
