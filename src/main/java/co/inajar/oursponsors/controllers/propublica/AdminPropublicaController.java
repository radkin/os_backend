package co.inajar.oursponsors.controllers.propublica;

import co.inajar.oursponsors.dbOs.entities.chamber.senate.Senator;
import co.inajar.oursponsors.models.propublica.SenatorResponse;
import co.inajar.oursponsors.services.propublica.MembersApiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/admin/propublica")
public class AdminPropublicaController {

    @Autowired
    private MembersApiManager membersApiManager;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "download_senators")
    public ResponseEntity<List<SenatorResponse>> downloadSenators() {
        var response = new ArrayList<SenatorResponse>();
        var httpStatus = HttpStatus.OK;
        var senators = membersApiManager.getSenatorsListResponse();
        var list = membersApiManager.mapPropublicaResponseToSenators(senators).parallelStream()
                .map(SenatorResponse::new)
                .collect(Collectors.toList());
        response.addAll(list);
        return new ResponseEntity<>(response, httpStatus);
    }
}
