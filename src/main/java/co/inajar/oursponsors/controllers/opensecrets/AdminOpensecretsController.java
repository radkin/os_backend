package co.inajar.oursponsors.controllers.opensecrets;

import co.inajar.oursponsors.models.fec.CommitteeRequest;
import co.inajar.oursponsors.models.opensecrets.CampaignResponse;
import co.inajar.oursponsors.models.opensecrets.contributor.ContributorResponse;
import co.inajar.oursponsors.models.opensecrets.sector.SectorResponse;
import co.inajar.oursponsors.services.opensecrets.CandidatesApiManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/opensecrets")
public class AdminOpensecretsController {

    @Autowired
    private CandidatesApiManager candidatesApiManager;

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "download_sectors/{part}")
    public ResponseEntity<List<SectorResponse>> downloadSectors(@PathVariable Integer part) {
        // NOTE: this will be a hack for now that accepts 1,2,3,4,5 for the part of our download
        // There are 400 and some change, total CIDs. Part 5 was 500 rows
        // as opensecrets.org only allows 200 downloads per day.
        var httpStatus = HttpStatus.OK;
        var sectorResponses = candidatesApiManager.getSectorsListResponse(part);
        var response = candidatesApiManager.mapOpenSecretsResponseToSectors(sectorResponses).stream()
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
        var contributorResponses = candidatesApiManager.getContributorsListResponse(part);
        var response = candidatesApiManager.mapOpenSecretsResponseToContributors(contributorResponses).stream()
                .map(ContributorResponse::new)
                .toList();
        return new ResponseEntity<>(response, httpStatus);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "download_campaign")
    public ResponseEntity<List<CampaignResponse>> downloadCampaign(@RequestBody CommitteeRequest data) {
        var httpStatus = HttpStatus.OK;
        var campaignResponses = candidatesApiManager.getCampaignListResponse(data);
//        var response = candidatesApiManager.mapHtmlParserResponseToCampaign(campaignResponses).stream()
//                .map(CampaignResponse::new)
//                .toList();
        var response = new ArrayList<CampaignResponse>();
        return new ResponseEntity<>(response, httpStatus);
    }
}
