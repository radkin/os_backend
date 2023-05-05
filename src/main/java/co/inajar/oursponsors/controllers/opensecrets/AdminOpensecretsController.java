package co.inajar.oursponsors.controllers.opensecrets;

import co.inajar.oursponsors.models.opensecrets.sector.SectorResponse;
import co.inajar.oursponsors.services.opensecrets.CandidatesApiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
    @RequestMapping(path="download_sectors/{part}", method = RequestMethod.GET)
    public ResponseEntity<List<SectorResponse>> downloadSectors(@PathVariable Integer part) {
        // NOTE: this will be a hack for now that accepts 1,2,3,4,5 for the part of our download
        // THere are 400 + change total CIDs
        // as opensecrets.org only allows 200 downloads per day.
        var httpStatus = HttpStatus.OK;
        var sectorResponses = candidatesApiManager.getSectorsListResponse(part);
        var response = candidatesApiManager.mapOpenSecretsResponseToSectors(sectorResponses).stream()
                .map(SectorResponse::new)
                .toList();
        candidatesApiManager.mapOpenSecretsResponseToSectors(sectorResponses);
        return new ResponseEntity<>(response, httpStatus);
    }

}
