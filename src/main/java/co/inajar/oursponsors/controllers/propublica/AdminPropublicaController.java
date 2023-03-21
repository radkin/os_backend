package co.inajar.oursponsors.controllers.propublica;

import co.inajar.oursponsors.models.propublica.congress.CongressResponse;
import co.inajar.oursponsors.models.propublica.senator.SenatorResponse;
import co.inajar.oursponsors.services.propublica.MembersApiManager;
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
@RequestMapping(path = "/admin/propublica")
public class AdminPropublicaController {

    @Autowired
    private MembersApiManager membersApiManager;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "download_senators")
    public ResponseEntity<List<SenatorResponse>> downloadSenators() {
        var response = new ArrayList<SenatorResponse>();
        var httpStatus = HttpStatus.OK;
        var senatorResponses = membersApiManager.getSenatorsListResponse();
        var list = membersApiManager.mapPropublicaResponseToSenators(senatorResponses).parallelStream()
                .map(SenatorResponse::new)
                .collect(Collectors.toList());
        response.addAll(list);
        return new ResponseEntity<>(response, httpStatus);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "download_congress")
    public ResponseEntity<List<CongressResponse>> downloadCongress() {
        var response = new ArrayList<CongressResponse>();
        var httpStatus = HttpStatus.OK;
        var congressResponses = membersApiManager.getCongressListResponse();
        var list = membersApiManager.mapPropublicaResponseToCongress(congressResponses).parallelStream()
                .map(CongressResponse::new)
                .collect(Collectors.toList());
        response.addAll(list);
        return new ResponseEntity<>(response, httpStatus);
    }
}
