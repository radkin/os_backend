package co.inajar.oursponsors.services.user;

import co.inajar.oursponsors.dbos.entities.User;
import co.inajar.oursponsors.models.user.UserRequest;

import java.util.Optional;

public interface UserManager {
    Optional<User> getUserById(Long id);

    Optional<User> getUserByApiKey(String apiKey);


    User createOrUpdateUser(UserRequest data, User user);

    Optional<User> getUserByGoogleUid(String googleUid);


}
