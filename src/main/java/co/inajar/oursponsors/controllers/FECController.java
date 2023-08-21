package co.inajar.oursponsors.controllers;

import co.inajar.oursponsors.models.fec.SponsorRequest;
import co.inajar.oursponsors.models.fec.SponsorResponse;
import co.inajar.oursponsors.services.fec.CommitteesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/fec")
public class FECController {

    @Autowired
    private CommitteesManager committeesManager;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "get_sponsors")
    public ResponseEntity<Set<SponsorResponse>> getSponsors(@RequestBody SponsorRequest data) {
        var response = new HashSet<SponsorResponse>();
        var httpStatus = HttpStatus.OK;
        var possibleSponsors = committeesManager.getSponsors(data);
        if (!possibleSponsors.isEmpty()) {
            var list = possibleSponsors.parallelStream()
                    .map(SponsorResponse::new)
                    .collect(Collectors.toSet());
            response.addAll(list);
        }
        return new ResponseEntity<>(response, httpStatus);
    }


}
