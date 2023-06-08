package co.inajar.oursponsors.models.opensecrets.contributor;

import co.inajar.oursponsors.dbos.entities.candidates.Contributor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SmallContributorResponse {

    @JsonProperty(value = "id")
    private Long id;
    @JsonProperty(value = "cid")
    private String cid;
    @JsonProperty(value = "org_name")
    private String orgName;
    @JsonProperty(value = "total")
    private Integer total;

    public SmallContributorResponse(Contributor contributor) {
        id = contributor.getId();
        cid = contributor.getCid();
        orgName = contributor.getOrgName();
        total = contributor.getTotal();
    }
}
