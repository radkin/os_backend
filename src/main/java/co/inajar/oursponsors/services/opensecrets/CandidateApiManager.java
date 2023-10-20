package co.inajar.oursponsors.services.opensecrets;

import co.inajar.oursponsors.dbos.entities.campaigns.Committee;
import co.inajar.oursponsors.dbos.entities.candidates.Contributor;
import co.inajar.oursponsors.dbos.entities.candidates.Sector;
import co.inajar.oursponsors.models.opensecrets.CampaignResponse;
import co.inajar.oursponsors.models.opensecrets.CommitteeRequest;
import co.inajar.oursponsors.models.opensecrets.contributor.OpenSecretsContributor;
import co.inajar.oursponsors.models.opensecrets.sector.OpenSecretsSector;

import java.util.List;

public interface CandidateApiManager {

    //    List<Sector> getSectorsListResponse();
    List<OpenSecretsSector> getSectorsListResponse(Integer part);

    List<Sector> mapOpenSecretsResponseToSectors(List<OpenSecretsSector> sectors);

    List<OpenSecretsSector> getOpenSecretsSector(String cid);

    // Contributors
    List<OpenSecretsContributor> getContributorsListResponse(Integer part);

    List<Contributor> mapOpenSecretsResponseToContributors(List<OpenSecretsContributor> contributors);

    List<OpenSecretsContributor> getOpenSecretsContributor(String cid);

    CampaignResponse getPresidentialCampaignListResponse(CommitteeRequest data);


    List<Committee> getCommittees();
}
