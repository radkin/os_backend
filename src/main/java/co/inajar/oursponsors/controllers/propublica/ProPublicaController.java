package co.inajar.oursponsors.controllers.propublica;

import co.inajar.oursponsors.models.propublica.congress.CongressResponse;
import co.inajar.oursponsors.models.propublica.GetMembersByStateRequest;
import co.inajar.oursponsors.models.propublica.senator.SenatorResponse;
import co.inajar.oursponsors.services.propublica.MembersManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/propublica")
public class ProPublicaController {

    @Autowired
    private MembersManager membersManager;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "get_senators")
    public ResponseEntity<List<SenatorResponse>> getSenators() {
        var response = new ArrayList<SenatorResponse>();
        var httpResponse = HttpStatus.OK;
        var list = membersManager.getSenators().parallelStream()
                .map(SenatorResponse::new)
                .collect(Collectors.toList());
        response.addAll(list);
        return new ResponseEntity<>(response, httpResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "get_senators_by_state")
    public ResponseEntity<List<SenatorResponse>> getSenatorsByState(@RequestBody GetMembersByStateRequest data) {
        var response = new ArrayList<SenatorResponse>();
        var httpResponse = HttpStatus.OK;
        var possibleSenators = membersManager.getSenatorsByState(data.getState());
        if (possibleSenators.isPresent()) {
            var list = possibleSenators.get().parallelStream()
                .map(SenatorResponse::new)
                .collect(Collectors.toList());
            response.addAll(list);
        }
        return new ResponseEntity<>(response, httpResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "get_congress")
    public ResponseEntity<List<CongressResponse>> getCongress() {
        var response = new ArrayList<CongressResponse>();
        var httpResponse = HttpStatus.OK;
        var list = membersManager.getCongress().parallelStream()
                .map(CongressResponse::new)
                .collect(Collectors.toList());
        response.addAll(list);
        return new ResponseEntity<>(response, httpResponse);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "get_congress_by_state")
    public ResponseEntity<List<CongressResponse>> getCongressesByState(@RequestBody GetMembersByStateRequest data) {
        var response = new ArrayList<CongressResponse>();
        var httpResponse = HttpStatus.OK;
        var possibleCongress = membersManager.getCongressByState(data.getState());
        if (possibleCongress.isPresent()) {
            var list = possibleCongress.get().parallelStream()
                    .map(CongressResponse::new)
                    .collect(Collectors.toList());
            response.addAll(list);
        }
        return new ResponseEntity<>(response, httpResponse);
    }
}
