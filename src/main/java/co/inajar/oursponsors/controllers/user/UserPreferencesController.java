package co.inajar.oursponsors.controllers.user;

import co.inajar.oursponsors.dbOs.entities.User;
import co.inajar.oursponsors.models.user.PreferencesRequest;
import co.inajar.oursponsors.models.user.PreferencesResponse;
import co.inajar.oursponsors.models.user.UserRequest;
import co.inajar.oursponsors.models.user.UserResponse;
import co.inajar.oursponsors.services.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/user")
public class UserPreferencesController {

    private static final String INAJAR_TOKEN = "inajar-token";
    @Autowired
    private UserManager userManager;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "get_preferences")
    public ResponseEntity<PreferencesResponse> getPreferences(@RequestBody PreferencesRequest data) {
        var httpResponse = HttpStatus.OK;
        var preferences = userManager.getPreferencesByUserId(data.getId());
        var response = new PreferencesResponse(preferences);
        return new ResponseEntity<>(response, httpResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "update_preferences")
    public ResponseEntity<PreferencesResponse> updatePreferences(@RequestBody PreferencesRequest data) {
        var httpResponse = HttpStatus.OK;
        var preferencesUpdate = userManager.updateUserPreferences(data);
        var response = new PreferencesResponse(preferencesUpdate);
        return new ResponseEntity<>(response, httpResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path="get_user")
    public ResponseEntity<UserResponse> getUser(@RequestHeader Map<String, String> headers) {
        var response = new UserResponse();
        var httpResponse = HttpStatus.OK;
        var possibleUser = userManager.getUserByApiKey(headers.get(INAJAR_TOKEN));
        if (possibleUser.isPresent()) {
            response = new UserResponse(possibleUser.get());
        }
        return new ResponseEntity<>(response, httpResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "update_user")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRequest data,
                                                   @RequestHeader Map<String, String> headers) {
        var response = new UserResponse();
        var httpResponse = HttpStatus.OK;
        var possibleUser = userManager.getUserByApiKey(headers.get(INAJAR_TOKEN));
        if (possibleUser.isPresent()) {
            User user = possibleUser.get();
            var userUpdate = userManager.updateUser(data, user);
            response = new UserResponse(userUpdate);
        }
        return new ResponseEntity<>(response, httpResponse);
    }
}
