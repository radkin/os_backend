package co.inajar.oursponsors.services.opensecrets;

import co.inajar.oursponsors.dbos.entities.candidates.Contributor;
import co.inajar.oursponsors.models.opensecrets.contributor.OpenSecretsContributor;

import java.util.List;
import java.util.Optional;

public interface ContributorManager {

    Contributor createContributor(OpenSecretsContributor openSecretsContributor);
    Optional<List<Contributor>> getContributorsByCid(String cid);

}
