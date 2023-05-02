package co.inajar.oursponsors.services.opensecrets;

import co.inajar.oursponsors.dbOs.entities.candidates.Sector;
import co.inajar.oursponsors.dbOs.entities.chambers.Congress;
import co.inajar.oursponsors.dbOs.entities.chambers.Senator;
import co.inajar.oursponsors.dbOs.repos.opensecrets.ContributionRepo;
import co.inajar.oursponsors.dbOs.repos.opensecrets.SectorRepo;
import co.inajar.oursponsors.dbOs.repos.propublica.CongressRepo;
import co.inajar.oursponsors.dbOs.repos.propublica.SenatorRepo;
import co.inajar.oursponsors.models.opensecrets.sector.CandSectorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Collection;

@Service
public class CandidatesApiImpl implements CandidatesApiManager {

    @Autowired
    private SectorRepo sectorRepo;

//    @Autowired
//    private ContributionRepo contributionRepo;

    @Autowired
    private SenatorRepo senatorRepo;

    @Autowired
    private CongressRepo congressRepo;

    private Logger logger = LoggerFactory.getLogger(CandidatesApiImpl.class);

    @Value("${OPENSECRETS_TOKEN")
    private String opensecretsApiKey;

    private List<Sector> getSectors() { return sectorRepo.findAll(); }

    private List<Senator> getSenators() { return senatorRepo.findAll(); }

    private List<Congress> getCongress() { return congressRepo.findAll(); }

    @Override
    public List<String> getAllCandSectorsFromOpenSecrets() {
        // get a list of all cids in propublica table
        // Todo: This should be a one off SQL query not a pull of all Members
        var senators = getSenators();
        var congress = getCongress();

        List<String> senatorCIDs = senators.parallelStream()
                .map(Senator::getCrpId)
                .collect(Collectors.toList());

        List<String> congressCIDs = congress.parallelStream()
                .map(Congress::getCrpId)
                .collect(Collectors.toList());

        List<String> allCIDs = Stream
                .of(senatorCIDs, congressCIDs)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

//        for (var cid : allCIDs) {
//            System.out.println("getting open secrets sectors for CID " + cid);
//        }

        // run getCandSectorResponse for each cid
        // run mapOpenSecretsResponseToSectors for each response
        return allCIDs;
    }

    // we need to run this for every CID that is listed on the propublica tables
//    private CandSectorResponse getCandSectorResponse(cid) {
//        var path = String.format("?method=candSector&cid=" + cid + "&cycle=2022&output=json");
//    }

    // before populating our DBs we need to use the client to get it from opensecrets
//    private List<Sector> mapOpenSecretsResponseToSectors(CandSectorResponse candSectorResponse) {
//        var sectorCids = getSectors().parallelStream()
//                .map(Sector::getCid)
//                .collect(Collectors.toList());
//
//        // get the cid from candSectorResponse
//
//        // mark these sectors as "NEW" if there are no existing cids that match
//
//        // mark these sectors as "UPDATE" if there is an existing cid match
//
//
//
//    }
}
