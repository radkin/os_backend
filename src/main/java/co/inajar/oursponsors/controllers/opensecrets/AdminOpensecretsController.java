package co.inajar.oursponsors.controllers.opensecrets;

import co.inajar.oursponsors.models.opensecrets.sector.OpenSecretsSector;
import co.inajar.oursponsors.models.opensecrets.sector.SectorResponse;
import co.inajar.oursponsors.services.opensecrets.CandidatesApiManager;
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
@RequestMapping(path = "/admin/opensecrets")
public class AdminOpensecretsController {

    @Autowired
    private CandidatesApiManager candidatesApiManager;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path="download_sectors")
    public ResponseEntity<List<SectorResponse>> downloadSectors() {
        // NOTE: this will be a hack for now that accepts 1, 2, or 3 for the part of our download
        // as opensecrets.org only allows 200 downloads per day.
        var response = new ArrayList<SectorResponse>();
        var httpStatus = HttpStatus.OK;
        var sectorResponses = candidatesApiManager.getSectorsListResponse();
        System.out.println(sectorResponses);
//        var list = candidatesApiManager.mapOpenSecretsResponseToSectors(sectorResponses).parallelStream()
//                .map(SectorResponse::new)
//                .collect(Collectors.toList());
//        response.addAll(list);
        candidatesApiManager.mapOpenSecretsResponseToSectors(sectorResponses);
        return new ResponseEntity<>(response, httpStatus);
    }

}
