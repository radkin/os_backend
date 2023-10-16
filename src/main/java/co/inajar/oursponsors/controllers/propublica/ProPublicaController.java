package co.inajar.oursponsors.controllers.propublica;

import co.inajar.oursponsors.models.propublica.congress.CongressDetailsResponse;
import co.inajar.oursponsors.models.propublica.congress.CongressResponse;
import co.inajar.oursponsors.models.propublica.congress.MiniCongressResponse;
import co.inajar.oursponsors.models.propublica.senator.MiniSenatorResponse;
import co.inajar.oursponsors.models.propublica.senator.SenatorDetailsResponse;
import co.inajar.oursponsors.models.propublica.senator.SenatorResponse;
import co.inajar.oursponsors.services.fec.SponsorsManager;
import co.inajar.oursponsors.services.preferences.PreferencesManager;
import co.inajar.oursponsors.services.propublica.MembersManager;
import co.inajar.oursponsors.services.user.UserManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/propublica")
public class ProPublicaController {

    private static final String UNABLE_TO_FIND_USER = "Unable to find User with Google UID {}";
    private static final String GOOGLE_UID = "google-uid";
    private Logger logger = LoggerFactory.getLogger(ProPublicaController.class);

    @Autowired
    private UserManager userManager;

    @Autowired
    private MembersManager membersManager;

    @Autowired
    private SponsorsManager sponsorsManager;

    @Autowired
    private PreferencesManager preferencesManager;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "get_senators")
    public ResponseEntity<List<SenatorResponse>> getSenators(@RequestHeader Map<String, String> headers) {
        var response = new ArrayList<SenatorResponse>();
        var httpResponse = HttpStatus.OK;

        var possibleUser = userManager.getUserByGoogleUid(headers.get(GOOGLE_UID));
        if (possibleUser.isPresent()) {
            var user = possibleUser.get();
            var possibleSenators = membersManager.getSenators(user);
            if (possibleSenators.isPresent()) {
                var list = possibleSenators.get().parallelStream()
                        .map(SenatorResponse::new)
                        .toList();
                response.addAll(list);
            }
        } else {
            logger.error(UNABLE_TO_FIND_USER, headers.get(GOOGLE_UID));
        }
        return new ResponseEntity<>(response, httpResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "get_senator_details/{id}")
    public ResponseEntity<SenatorDetailsResponse> getSenatorDetails(@PathVariable String id, @RequestHeader Map<String, String> headers) {
        var response = new SenatorDetailsResponse();
        var httpStatus = HttpStatus.OK;

        var possibleUser = userManager.getUserByGoogleUid(headers.get(GOOGLE_UID));
        if (possibleUser.isPresent()) {
            var preferences = preferencesManager.getPreferencesByUserId(possibleUser.get().getId());
            var possibleSenator = membersManager.getSenatorById(Long.valueOf(id));
            if (possibleSenator.isPresent()) {
                var senator = possibleSenator.get();
                var possibleSenatorDetailsResponse = membersManager.gatherSenatorDetailsResponse(senator, preferences);
                if (possibleSenatorDetailsResponse.isPresent()) {
                    response = possibleSenatorDetailsResponse.get();
                }
            }
        } else {
            logger.error(UNABLE_TO_FIND_USER, headers.get(GOOGLE_UID));
        }


        return new ResponseEntity<>(response, httpStatus);
    }


    // ToDo: reverse the order of our Sponsor results.
    // for now, just use the 2nd element and then the 1st in our front end
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "get_mini_senators")
    public ResponseEntity<List<MiniSenatorResponse>> getMiniSenators(@RequestHeader Map<String, String> headers) {
        var response = new ArrayList<MiniSenatorResponse>();
        var httpResponse = HttpStatus.OK;

        var possibleUser = userManager.getUserByGoogleUid(headers.get(GOOGLE_UID));
        if (possibleUser.isPresent()) {
            var user = possibleUser.get();
            var possibleSenators = membersManager.getSenators(user);
            if (possibleSenators.isPresent()) {
                var list = possibleSenators.get().parallelStream()
                        .map(senator -> new MiniSenatorResponse(senator, sponsorsManager))
                        .toList();
                response.addAll(list);
            }
        } else {
            logger.error(UNABLE_TO_FIND_USER, headers.get(GOOGLE_UID));
        }
        return new ResponseEntity<>(response, httpResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "get_congress")
    public ResponseEntity<List<CongressResponse>> getCongresses(@RequestHeader Map<String, String> headers) {
        var response = new ArrayList<CongressResponse>();
        var httpResponse = HttpStatus.OK;

        var possibleUser = userManager.getUserByGoogleUid(headers.get(GOOGLE_UID));
        if (possibleUser.isPresent()) {
            var user = possibleUser.get();
            var possibleCongress = membersManager.getCongress(user);
            if (possibleCongress.isPresent()) {
                var list = possibleCongress.get().parallelStream()
                        .map(CongressResponse::new)
                        .toList();
                response.addAll(list);
            }
        } else {
            logger.error(UNABLE_TO_FIND_USER, headers.get(GOOGLE_UID));
        }
        return new ResponseEntity<>(response, httpResponse);
    }

    // ToDo: reverse the order of our Sponsor results.
    // for now, just use the 2nd element and then the 1st in our front end
    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "get_mini_congress")
    public ResponseEntity<List<MiniCongressResponse>> getMiniCongresses(@RequestHeader Map<String, String> headers) {
        var response = new ArrayList<MiniCongressResponse>();
        var httpResponse = HttpStatus.OK;

        var possibleUser = userManager.getUserByGoogleUid(headers.get(GOOGLE_UID));
        if (possibleUser.isPresent()) {
            var user = possibleUser.get();
            var possibleCongress = membersManager.getCongress(user);
            if (possibleCongress.isPresent()) {
                var list = possibleCongress.get().parallelStream()
                        .map(congress -> new MiniCongressResponse(congress, sponsorsManager))
                        .toList();
                response.addAll(list);
            }
        } else {
            logger.error(UNABLE_TO_FIND_USER, headers.get(GOOGLE_UID));
        }
        return new ResponseEntity<>(response, httpResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "get_congress_details/{id}")
    public ResponseEntity<CongressDetailsResponse> getCongressDetails(@PathVariable String id, @RequestHeader Map<String, String> headers) {
        var response = new CongressDetailsResponse();
        var httpStatus = HttpStatus.OK;

        var possibleUser = userManager.getUserByGoogleUid(headers.get(GOOGLE_UID));
        if (possibleUser.isPresent()) {
            var preferences = preferencesManager.getPreferencesByUserId(possibleUser.get().getId());
            var possibleCongress = membersManager.getCongressById(Long.valueOf(id));
            if (possibleCongress.isPresent()) {
                var congress = possibleCongress.get();
                var possibleCongressDetailsResponse = membersManager.gatherCongressDetailsResponse(congress, preferences);
                if (possibleCongressDetailsResponse.isPresent()) {
                    response = possibleCongressDetailsResponse.get();
                }
            }
        } else {
            logger.error(UNABLE_TO_FIND_USER, headers.get(GOOGLE_UID));
        }


        return new ResponseEntity<>(response, httpStatus);
    }


}
