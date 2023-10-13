package co.inajar.oursponsors.controllers.user;

import co.inajar.oursponsors.dbos.entities.User;
import co.inajar.oursponsors.models.user.PreferencesRequest;
import co.inajar.oursponsors.models.user.PreferencesResponse;
import co.inajar.oursponsors.models.user.UserRequest;
import co.inajar.oursponsors.models.user.UserResponse;
import co.inajar.oursponsors.services.preferences.PreferencesManager;
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
    private static final String GOOGLE_UID = "google-uid";
    private static final String CREATING_USER = "Creating new User";
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserManager userManager;

    @Autowired
    private PreferencesManager preferencesManager;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "get_preferences")
    public ResponseEntity<PreferencesResponse> getPreferences(@RequestHeader Map<String, String> headers) {
        var response = new PreferencesResponse();
        var httpResponse = HttpStatus.OK;
        var possibleUser = userManager.getUserByGoogleUid(headers.get(GOOGLE_UID));
        if (possibleUser.isPresent()) {
            var preferences = preferencesManager.getPreferencesByUserId(possibleUser.get().getId());
            response = new PreferencesResponse(preferences);
        } else {
            logger.error(UNABLE_TO_FIND_USER, headers.get(GOOGLE_UID));
        }


        return new ResponseEntity<>(response, httpResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "update_preferences")
    public ResponseEntity<PreferencesResponse> updatePreferences(@RequestBody PreferencesRequest data,
                                                                 @RequestHeader Map<String, String> headers) {
        var response = new PreferencesResponse();
        var httpResponse = HttpStatus.OK;
        var possibleUser = userManager.getUserByGoogleUid(headers.get(GOOGLE_UID));
        if (possibleUser.isPresent()) {
            var preferencesUpdate = preferencesManager.updateUserPreferences(data, possibleUser.get().getId());
            response = new PreferencesResponse(preferencesUpdate);
        } else {
            logger.error(UNABLE_TO_FIND_USER, headers.get(GOOGLE_UID));
        }


        return new ResponseEntity<>(response, httpResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "get_user")
    public ResponseEntity<UserResponse> getUser(@RequestHeader Map<String, String> headers) {
        var response = new UserResponse();
        var httpResponse = HttpStatus.OK;

        var possibleUser = userManager.getUserByGoogleUid(headers.get(GOOGLE_UID));
        if (possibleUser.isPresent()) {
            response = new UserResponse(possibleUser.get());
        } else {
            logger.error(UNABLE_TO_FIND_USER, headers.get(GOOGLE_UID));
        }
        return new ResponseEntity<>(response, httpResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "create_or_update_user")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest data,
                                                   @RequestHeader Map<String, String> headers) {
        var response = new UserResponse();
        var httpResponse = HttpStatus.OK;
        var possibleUser = userManager.getUserByGoogleUid(headers.get(GOOGLE_UID));
        if (possibleUser.isPresent()) {

            User user = possibleUser.get();
            var userUpdate = userManager.createOrUpdateUser(data, user);
            response = new UserResponse(userUpdate);
        } else {
            logger.info(UNABLE_TO_FIND_USER, headers.get(GOOGLE_UID), CREATING_USER);
//            User user = new User();
//            // default state & party
//            user.setState("IL");
//            user.setParty("R");
//            var createUser = userManager.createOrUpdateUser(data, user);
//            userManager.createUserPreferences(user);
//            response = new UserResponse(createUser);
        }
        return new ResponseEntity<>(response, httpResponse);
    }
}

