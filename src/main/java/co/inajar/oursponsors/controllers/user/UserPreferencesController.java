package co.inajar.oursponsors.controllers.user;

import co.inajar.oursponsors.models.user.PreferencesRequest;
import co.inajar.oursponsors.models.user.PreferencesResponse;
import co.inajar.oursponsors.services.user.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")
public class UserPreferencesController {

    @Autowired
    private UserManager userManager;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "get_preferences")
    public ResponseEntity<PreferencesResponse> getPreferences(@RequestBody PreferencesRequest data) {
        var response = new PreferencesResponse();
        var httpResponse = HttpStatus.OK;
        var preferences = userManager.getPreferencesByUserId(data.getId());
        response = new PreferencesResponse(preferences);
        return new ResponseEntity<>(response, httpResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "update_preferences")
    public ResponseEntity<PreferencesResponse> updatePreferences(@RequestBody PreferencesRequest data) {
        var response = new PreferencesResponse();
        var httpResponse = HttpStatus.OK;
        var preferencesUpdate = userManager.updateUserPreferences(data);
        response = new PreferencesResponse(preferencesUpdate);
        return new ResponseEntity<>(response, httpResponse);
    }
}
