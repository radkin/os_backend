package co.inajar.oursponsors.controllers.propublica;

import co.inajar.oursponsors.models.propublica.congress.CongressDetailsResponse;
import co.inajar.oursponsors.models.propublica.congress.CongressResponse;
import co.inajar.oursponsors.models.propublica.congress.MiniCongressResponse;
import co.inajar.oursponsors.models.propublica.senator.MiniSenatorResponse;
import co.inajar.oursponsors.models.propublica.senator.SenatorDetailsResponse;
import co.inajar.oursponsors.models.propublica.senator.SenatorResponse;
import co.inajar.oursponsors.services.fec.SponsorManager;
import co.inajar.oursponsors.services.preferences.PreferenceManager;
import co.inajar.oursponsors.services.propublica.MemberManager;
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

    // Note: Google UID and INAJAR-TOKEN are the same
    private static final String UNABLE_TO_FIND_USER = "Unable to find User with Google UID {}";
    private static final String INAJAR_TOKEN = "inajar-token";
    private Logger logger = LoggerFactory.getLogger(ProPublicaController.class);

    @Autowired
    private UserManager userManager;

    @Autowired
    private MemberManager memberManager;

    @Autowired
    private SponsorManager sponsorManager;

    @Autowired
    private PreferenceManager preferenceManager;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "get_senators")
    public ResponseEntity<List<SenatorResponse>> getSenators(@RequestHeader Map<String, String> headers) {
        var response = new ArrayList<SenatorResponse>();
        var httpResponse = HttpStatus.OK;

        var possibleUser = userManager.getUserByApiKey(headers.get(INAJAR_TOKEN));
        if (possibleUser.isPresent()) {
            var user = possibleUser.get();
            var possibleSenators = memberManager.getSenators(user);
            if (possibleSenators.isPresent()) {
                var list = possibleSenators.get().parallelStream()
                        .map(SenatorResponse::new)
                        .toList();
                response.addAll(list);
            }
        } else {
            logger.error(UNABLE_TO_FIND_USER, headers.get(INAJAR_TOKEN));
        }
        return new ResponseEntity<>(response, httpResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "get_senator_details/{id}")
    public ResponseEntity<SenatorDetailsResponse> getSenatorDetails(@PathVariable String id, @RequestHeader Map<String, String> headers) {
        var response = new SenatorDetailsResponse();
        var httpStatus = HttpStatus.OK;

        var possibleUser = userManager.getUserByApiKey(headers.get(INAJAR_TOKEN));
        if (possibleUser.isPresent()) {
            var preferences = preferenceManager.getPreferencesByUserId(possibleUser.get().getId());
            var possibleSenator = memberManager.getSenatorById(Long.valueOf(id));
            if (possibleSenator.isPresent()) {
                var senator = possibleSenator.get();
                var possibleSenatorDetailsResponse = memberManager.gatherSenatorDetailsResponse(senator, preferences);
                if (possibleSenatorDetailsResponse.isPresent()) {
                    response = possibleSenatorDetailsResponse.get();
                }
            }
        } else {
            logger.error(UNABLE_TO_FIND_USER, headers.get(INAJAR_TOKEN));
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

        var possibleUser = userManager.getUserByApiKey(headers.get(INAJAR_TOKEN));
        if (possibleUser.isPresent()) {
            var user = possibleUser.get();
            var possibleSenators = memberManager.getSenators(user);
            if (possibleSenators.isPresent()) {
                var list = possibleSenators.get().parallelStream()
                        .map(senator -> new MiniSenatorResponse(senator, sponsorManager))
                        .toList();
                response.addAll(list);
            }
        } else {
            logger.error(UNABLE_TO_FIND_USER, headers.get(INAJAR_TOKEN));
        }
        return new ResponseEntity<>(response, httpResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "get_congress")
    public ResponseEntity<List<CongressResponse>> getCongresses(@RequestHeader Map<String, String> headers) {
        var response = new ArrayList<CongressResponse>();
        var httpResponse = HttpStatus.OK;

        var possibleUser = userManager.getUserByApiKey(headers.get(INAJAR_TOKEN));
        if (possibleUser.isPresent()) {
            var user = possibleUser.get();
            var possibleCongress = memberManager.getCongress(user);
            if (possibleCongress.isPresent()) {
                var list = possibleCongress.get().parallelStream()
                        .map(CongressResponse::new)
                        .toList();
                response.addAll(list);
            }
        } else {
            logger.error(UNABLE_TO_FIND_USER, headers.get(INAJAR_TOKEN));
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

        var possibleUser = userManager.getUserByApiKey(headers.get(INAJAR_TOKEN));
        if (possibleUser.isPresent()) {
            var user = possibleUser.get();
            var possibleCongress = memberManager.getCongress(user);
            if (possibleCongress.isPresent()) {
                var list = possibleCongress.get().parallelStream()
                        .map(congress -> new MiniCongressResponse(congress, sponsorManager))
                        .toList();
                response.addAll(list);
            }
        } else {
            logger.error(UNABLE_TO_FIND_USER, headers.get(INAJAR_TOKEN));
        }
        return new ResponseEntity<>(response, httpResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(value = "get_congress_details/{id}")
    public ResponseEntity<CongressDetailsResponse> getCongressDetails(@PathVariable String id, @RequestHeader Map<String, String> headers) {
        var response = new CongressDetailsResponse();
        var httpStatus = HttpStatus.OK;

        var possibleUser = userManager.getUserByApiKey(headers.get(INAJAR_TOKEN));
        if (possibleUser.isPresent()) {
            var preferences = preferenceManager.getPreferencesByUserId(possibleUser.get().getId());
            var possibleCongress = memberManager.getCongressById(Long.valueOf(id));
            if (possibleCongress.isPresent()) {
                var congress = possibleCongress.get();
                var possibleCongressDetailsResponse = memberManager.gatherCongressDetailsResponse(congress, preferences);
                if (possibleCongressDetailsResponse.isPresent()) {
                    response = possibleCongressDetailsResponse.get();
                }
            }
        } else {
            logger.error(UNABLE_TO_FIND_USER, headers.get(INAJAR_TOKEN));
        }


        return new ResponseEntity<>(response, httpStatus);
    }


}
