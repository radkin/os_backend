package co.inajar.oursponsors.controllers.opensecrets;

import co.inajar.oursponsors.models.opensecrets.CommitteeResponse;
import co.inajar.oursponsors.models.opensecrets.contributor.ContributorRequest;
import co.inajar.oursponsors.models.opensecrets.contributor.OpenSecretsContributor;
import co.inajar.oursponsors.models.opensecrets.contributor.SmallContributorResponse;
import co.inajar.oursponsors.models.opensecrets.sector.OpenSecretsSector;
import co.inajar.oursponsors.models.opensecrets.sector.SectorRequest;
import co.inajar.oursponsors.models.opensecrets.sector.SmallSectorResponse;
import co.inajar.oursponsors.services.fec.CommitteeManager;
import co.inajar.oursponsors.services.opensecrets.CandidateManager;
import co.inajar.oursponsors.services.opensecrets.ContributorManager;
import co.inajar.oursponsors.services.opensecrets.SectorManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/opensecrets")
public class OpenSecretsController {

    @Autowired
    private ContributorManager contributorManager;

    @Autowired
    private CandidateManager candidateManager;

    @Autowired
    SectorManager sectorManager;

    @Autowired
    private CommitteeManager committeeManager;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "get_sectors")
    public ResponseEntity<List<SmallSectorResponse>> getSectors(@RequestBody SectorRequest data) {
        var response = new ArrayList<SmallSectorResponse>();
        var httpStatus = HttpStatus.OK;
        var possibleSectors = sectorManager.getSectorsByCid(data.getCid());
        if (possibleSectors.isPresent() && !possibleSectors.get().isEmpty()) {
            var list = possibleSectors.get().parallelStream()
                    .map(SmallSectorResponse::new)
                    .toList();
            response.addAll(list);
        } else {
//            // ToDo: remove this "on demand" gathering when we have a complete table
            var openSecretsSectors = new ArrayList<OpenSecretsSector>();
            var possibleOnDemandSectors = Optional.ofNullable(candidateManager.getOpenSecretsSector(data.getCid()));
            possibleOnDemandSectors.ifPresent(openSecretsSectors::addAll);
            var list = sectorManager.mapOpenSecretsResponseToSectors(openSecretsSectors).stream()
                    .map(SmallSectorResponse::new)
                    .toList();
            response.addAll(list);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping(path = "get_contributors")
    public ResponseEntity<List<SmallContributorResponse>> getContributors(@RequestBody ContributorRequest data) {
        var response = new ArrayList<SmallContributorResponse>();
        var httpStatus = HttpStatus.OK;
        var possibleContributors = contributorManager.getContributorsByCid(data.getCid());
        if (possibleContributors.isPresent() && !possibleContributors.get().isEmpty()) {
            var list = possibleContributors.get().parallelStream()
                    .map(SmallContributorResponse::new)
                    .toList();
            response.addAll(list);
        } else {
//            // ToDo: remove this "on demand" gathering when we have a complete table
            var openSecretsContributors = new ArrayList<OpenSecretsContributor>();
            var possibleOnDemandContributors = Optional.ofNullable(candidateManager.getOpenSecretsContributor(data.getCid()));
            possibleOnDemandContributors.ifPresent(openSecretsContributors::addAll);
            var list = candidateManager.mapOpenSecretsResponseToContributors(openSecretsContributors).stream()
                    .map(SmallContributorResponse::new)
                    .toList();
            response.addAll(list);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping(path = "get_committees")
    public ResponseEntity<List<CommitteeResponse>> getCommittees() {
        var response = new ArrayList<CommitteeResponse>();
        var httpStatus = HttpStatus.OK;
        var committees = committeeManager.getCommittees();
        var list = committees.parallelStream()
                .map(CommitteeResponse::new)
                .toList();
        response.addAll(list);
        return new ResponseEntity<>(response, httpStatus);
    }

}
