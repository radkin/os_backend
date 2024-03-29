package co.inajar.oursponsors.controllers.fec;

import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.models.fec.FecCommitteeRequest;
import co.inajar.oursponsors.models.opensecrets.CampaignResponse;
import co.inajar.oursponsors.services.fec.CommitteeApiManager;
import co.inajar.oursponsors.services.propublica.MemberManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping(path = "/admin/fec")
public class AdminFECController {

    @Autowired
    private CommitteeApiManager committeeApiManager;
    @Autowired
    private MemberManager memberManager;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "download_campaign")
    public ResponseEntity<CampaignResponse> downloadCampaign(@RequestBody FecCommitteeRequest data) {
        var response = new CampaignResponse();
        var httpStatus = HttpStatus.OK;
        if (data.getChamber().equals("senator")) {
            Optional<Senator> possibleSenator = memberManager.getSenatorById(data.getOsId());
            if (possibleSenator.isPresent()) {
                response = committeeApiManager.getSenatorCampaignListResponse(possibleSenator.get());
            }
        } else if (data.getChamber().equals("congress")) {
            Optional<Congress> possibleCongress = memberManager.getCongressById(data.getOsId());
            if (possibleCongress.isPresent()) {
                response = committeeApiManager.getCongressCampaignListResponse(possibleCongress.get());
            }
        } else {
            System.out.println("Please choose senator or congress");
        }

        return new ResponseEntity<>(response, httpStatus);
    }

}
