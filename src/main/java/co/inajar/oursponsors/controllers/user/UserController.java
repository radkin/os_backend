package co.inajar.oursponsors.controllers.user;

import co.inajar.oursponsors.dbos.entities.User;
import co.inajar.oursponsors.models.user.PreferencesRequest;
import co.inajar.oursponsors.models.user.PreferencesResponse;
import co.inajar.oursponsors.models.user.UserRequest;
import co.inajar.oursponsors.models.user.UserResponse;
import co.inajar.oursponsors.services.preferences.PreferenceManager;
import co.inajar.oursponsors.services.user.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    private static final String UNABLE_TO_FIND_USER = "Unable to find User with Google UID {}";
    private static final String INAJAR_TOKEN = "inajar-token";
    private static final String CREATING_USER = "Creating new User";
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserManager userManager;

    @Autowired
    private PreferenceManager preferenceManager;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "get_preferences")
    public ResponseEntity<PreferencesResponse> getPreferences(@RequestHeader Map<String, String> headers) {
        var response = new PreferencesResponse();
        var httpResponse = HttpStatus.OK;
        var possibleUser = userManager.getUserByApiKey(headers.get(INAJAR_TOKEN));
        if (possibleUser.isPresent()) {
            var preferences = preferenceManager.getPreferencesByUserId(possibleUser.get().getId());
            response = new PreferencesResponse(preferences);
        } else {
            logger.error(UNABLE_TO_FIND_USER, headers.get(INAJAR_TOKEN));
        }


        return new ResponseEntity<>(response, httpResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "update_preferences")
    public ResponseEntity<PreferencesResponse> updatePreferences(@RequestBody PreferencesRequest data,
                                                                 @RequestHeader Map<String, String> headers) {
        var response = new PreferencesResponse();
        var httpResponse = HttpStatus.OK;
        var possibleUser = userManager.getUserByApiKey(headers.get(INAJAR_TOKEN));
        if (possibleUser.isPresent()) {
            var preferencesUpdate = preferenceManager.updateUserPreferences(data, possibleUser.get().getId());
            response = new PreferencesResponse(preferencesUpdate);
        } else {
            logger.error(UNABLE_TO_FIND_USER, headers.get(INAJAR_TOKEN));
        }


        return new ResponseEntity<>(response, httpResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "get_user")
    public ResponseEntity<UserResponse> getUser(@RequestHeader Map<String, String> headers) {
        var response = new UserResponse();
        var httpResponse = HttpStatus.OK;

        var possibleUser = userManager.getUserByApiKey(headers.get(INAJAR_TOKEN));
        if (possibleUser.isPresent()) {
            response = new UserResponse(possibleUser.get());
        } else {
            logger.error(UNABLE_TO_FIND_USER, headers.get(INAJAR_TOKEN));
        }
        return new ResponseEntity<>(response, httpResponse);
    }

    // Note: User ID 1 is the default user. It is there for adding new users and never changes.
    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "create_or_update_user")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest data,
                                                   @RequestHeader Map<String, String> headers) {
        var response = new UserResponse();
        var httpResponse = HttpStatus.OK;
        var possibleUser = userManager.getUserByApiKey(headers.get(INAJAR_TOKEN));
        if (possibleUser.isPresent()) {
            User foundUser = possibleUser.get();
            // Note: the below code assumes we have changed inajar-token to the new user!
            if (!foundUser.getId().equals(1L)) {
                var userUpdate = userManager.createOrUpdateUser(data, foundUser);
                response = new UserResponse(userUpdate);
            } else if (!data.getInajarApiKey().equals(foundUser.getApiKey())) {
                logger.info(UNABLE_TO_FIND_USER, headers.get(INAJAR_TOKEN), CREATING_USER);
                User user = new User();
                user.setEmail(data.getEmail());
                user.setFirstName(data.getFirstName());
                user.setLastName(data.getLastName());
                // default state & party
                user.setState("IL");
                user.setParty("R");
                user.setApiKey(data.getInajarApiKey());
                var createUser = userManager.createOrUpdateUser(data, user);
                userManager.createUserPreferences(createUser);
                response = new UserResponse(createUser);
            }
        }
        return new ResponseEntity<>(response, httpResponse);
    }

}