package co.inajar.oursponsors.controllers.opensecrets;

import co.inajar.oursponsors.models.opensecrets.sector.SectorRequest;
import co.inajar.oursponsors.models.opensecrets.sector.SectorResponse;
import co.inajar.oursponsors.models.opensecrets.sector.SmallSectorResponse;
import co.inajar.oursponsors.services.opensecrets.CandidatesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/opensecrets")
public class OpenSecretsController {

    @Autowired
    private CandidatesManager candidatesManager;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path="get_sectors")
    public ResponseEntity<List<SmallSectorResponse>> getSectors(@RequestBody SectorRequest data) {
        var response = new ArrayList<SmallSectorResponse>();
        var httpStatus = HttpStatus.OK;
        var possibleSectors = candidatesManager.getSectorsByCid(data.getCid());
        if (possibleSectors.isPresent()) {
            var list = possibleSectors.get().parallelStream()
                .map(SmallSectorResponse::new)
                .collect(Collectors.toList());
            response.addAll(list);
        }

//        System.out.println(response);
        return new ResponseEntity<>(response, httpStatus);
    }

}
