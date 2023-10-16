package co.inajar.oursponsors.controllers.opensecrets;

import co.inajar.oursponsors.models.opensecrets.CampaignResponse;
import co.inajar.oursponsors.models.opensecrets.CommitteeRequest;
import co.inajar.oursponsors.models.opensecrets.contributor.ContributorResponse;
import co.inajar.oursponsors.models.opensecrets.sector.SectorResponse;
import co.inajar.oursponsors.services.opensecrets.CandidateManager;
import co.inajar.oursponsors.services.opensecrets.SectorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/opensecrets")
public class AdminOpensecretsController {

    @Autowired
    private CandidateManager candidateManager;

    @Autowired
    private SectorManager sectorManager;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "download_sectors/{part}")
    public ResponseEntity<List<SectorResponse>> downloadSectors(@PathVariable Integer part) {
        // NOTE: this will be a hack for now that accepts 1,2,3,4,5 for the part of our download
        // There are 400 and some change, total CIDs. Part 5 was 500 rows
        // as opensecrets.org only allows 200 downloads per day.
        var httpStatus = HttpStatus.OK;
        var sectorResponses = candidateManager.getSectorsListResponse(part);
        var response = sectorManager.mapOpenSecretsResponseToSectors(sectorResponses).stream()
                .map(SectorResponse::new)
                .toList();
        return new ResponseEntity<>(response, httpStatus);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "download_contributors/{part}")
    public ResponseEntity<List<ContributorResponse>> downloadContributors(@PathVariable Integer part) {
        // NOTE: this will be a hack for now that accepts 1,2,3,4,5 for the part of our download
        // There are 400 and some change, total CIDs. Part 5 was 500 rows
        // as opensecrets.org only allows 200 downloads per day.
        var httpStatus = HttpStatus.OK;
        var contributorResponses = candidateManager.getContributorsListResponse(part);
        var response = candidateManager.mapOpenSecretsResponseToContributors(contributorResponses).stream()
                .map(ContributorResponse::new)
                .toList();
        return new ResponseEntity<>(response, httpStatus);
    }

    /* NOTE: this is really both OpenSecrets and FEC */
    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "download_campaign")
    public ResponseEntity<CampaignResponse> downloadCampaign(@RequestBody CommitteeRequest data) {
        var response = new CampaignResponse();
        var httpStatus = HttpStatus.OK;
        response = candidateManager.getPresidentialCampaignListResponse(data);

        return new ResponseEntity<>(response, httpStatus);
    }
}
