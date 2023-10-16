package co.inajar.oursponsors.services.opensecrets;

import co.inajar.oursponsors.dbos.entities.candidates.Contributor;
import co.inajar.oursponsors.dbos.repos.opensecrets.ContributorRepo;
import co.inajar.oursponsors.models.opensecrets.contributor.OpenSecretsContributor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContributorManagerImpl implements ContributorManager {

    @Autowired
    private ContributorRepo contributorRepo;

    public Contributor createContributor(OpenSecretsContributor openSecretsContributor) {
        var newContributor = new Contributor();
        var nc = setContributor(newContributor, openSecretsContributor);
        return contributorRepo.save(nc);
    }

    private Contributor setContributor(Contributor contributor, OpenSecretsContributor osc) {
        contributor.setCid(osc.getCid());
        contributor.setCycle(osc.getCycle());
        contributor.setOrgName(osc.getOrgName());
        contributor.setContributorId(osc.getContributorId());
        contributor.setIndivs(Integer.valueOf(osc.getIndivs()));
        contributor.setPacs(Integer.valueOf(osc.getPacs()));
        contributor.setTotal(Integer.valueOf(osc.getTotal()));
        return contributor;
    }

    @Override
    public Optional<List<Contributor>> getContributorsByCid(String cid) {
        return contributorRepo.findTop10ContributorsByCidOrderByTotalDesc(cid);
    }
}
