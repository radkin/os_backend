package co.inajar.oursponsors.controllers.opensecrets;

import co.inajar.oursponsors.models.opensecrets.sector.OpenSecretsSector;
import co.inajar.oursponsors.models.opensecrets.sector.SectorRequest;
import co.inajar.oursponsors.models.opensecrets.sector.SectorResponse;
import co.inajar.oursponsors.models.opensecrets.sector.SmallSectorResponse;
import co.inajar.oursponsors.services.opensecrets.CandidatesApiManager;
import co.inajar.oursponsors.services.opensecrets.CandidatesManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/opensecrets")
public class OpenSecretsController {

    @Autowired
    private CandidatesManager candidatesManager;

    @Autowired
    private CandidatesApiManager candidatesApiManager;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path="get_sectors")
    public ResponseEntity<List<SmallSectorResponse>> getSectors(@RequestBody SectorRequest data) {
        var response = new ArrayList<SmallSectorResponse>();
        var httpStatus = HttpStatus.OK;
        var possibleSectors = candidatesManager.getSectorsByCid(data.getCid());
        System.out.println("Gathering sectors for " + data.getCid());
        if (possibleSectors.isPresent() && !possibleSectors.isEmpty() && possibleSectors.get().size() != 0) {
            var list = possibleSectors.get().parallelStream()
                .map(SmallSectorResponse::new)
                .collect(Collectors.toList());
            response.addAll(list);
            System.out.println("Found existing sectors in the DB!");
            System.out.println(response);
        } else {
            System.out.println("No sectors present. Running on demand");
            var openSecretsSectors = new ArrayList<OpenSecretsSector>();
            var possibleOnDemandSectors = Optional.ofNullable(candidatesApiManager.getOpenSecretsSector(data.getCid()));
            possibleOnDemandSectors.ifPresent(openSecretsSectors::addAll);
            var list = candidatesApiManager.mapOpenSecretsResponseToSectors(openSecretsSectors).stream()
                    .map(SmallSectorResponse::new)
                    .toList();
            response.addAll(list);
            System.out.println(response);
        }

        return new ResponseEntity<>(response, httpStatus);
    }

}
