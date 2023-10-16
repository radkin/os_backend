package co.inajar.oursponsors.controllers.fec;

import co.inajar.oursponsors.models.fec.SponsorRequest;
import co.inajar.oursponsors.models.fec.SponsorResponse;
import co.inajar.oursponsors.services.fec.SponsorsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/fec")
public class FECController {

    @Autowired
    private SponsorsManager sponsorsManager;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "get_sponsors")
    public ResponseEntity<List<SponsorResponse>> getSponsors(@RequestBody SponsorRequest data) {
        var response = new ArrayList<SponsorResponse>();
        var httpStatus = HttpStatus.OK;
        var possibleSponsors = sponsorsManager.getSponsors(data);
        if (!possibleSponsors.isEmpty()) {
            var list = possibleSponsors.parallelStream()
                    .map(SponsorResponse::new)
                    .collect(Collectors.toList());
            response.addAll(list);
        }
        return new ResponseEntity<>(response, httpStatus);
    }


}
