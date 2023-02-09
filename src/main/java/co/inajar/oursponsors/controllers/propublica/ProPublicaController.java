package co.inajar.oursponsors.controllers.propublica;

import co.inajar.oursponsors.models.SenatorResponse;
import co.inajar.oursponsors.services.propublica.MembersManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}