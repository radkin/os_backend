package co.inajar.oursponsors.controllers.propublica;

import co.inajar.oursponsors.dbOs.entities.User;
import co.inajar.oursponsors.models.propublica.congress.CongressResponse;
import co.inajar.oursponsors.models.propublica.senator.SenatorResponse;
import co.inajar.oursponsors.services.propublica.MembersManager;
import co.inajar.oursponsors.services.user.UserManager;
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

    private static final String INAJAR_TOKEN = "inajar-token";

    @Autowired
    private UserManager userManager;

    @Autowired
    private MembersManager membersManager;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "get_senators")
    public ResponseEntity<List<SenatorResponse>> getSenators(@RequestHeader Map<String, String> headers) {
        var response = new ArrayList<SenatorResponse>();
        var httpResponse = HttpStatus.OK;

        var possibleUser = userManager.getUserByApiKey(headers.get(INAJAR_TOKEN));
        if (possibleUser.isPresent()) {
            var user = possibleUser.get();
            var possibleSenators = membersManager.getSenators(user);
            if (possibleSenators.isPresent()) {
                var list = possibleSenators.get().parallelStream()
                        .map(SenatorResponse::new)
                        .toList();
                response.addAll(list);
            }
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
            var possibleCongress = membersManager.getCongress(user);
            if (possibleCongress.isPresent()) {
                var list = possibleCongress.get().parallelStream()
                        .map(CongressResponse::new)
                        .toList();
                response.addAll(list);
            }
        }
        return new ResponseEntity<>(response, httpResponse);
    }
}
