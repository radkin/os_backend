package co.inajar.oursponsors.models.opensecrets.contributor;

import co.inajar.oursponsors.dbOs.entities.candidates.Contributor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SmallContributorResponse {

    public SmallContributorResponse(Contributor contributor) {
        id = contributor.getId();
        cid = contributor.getCid();
        orgName = contributor.getOrgName();
        total = contributor.getTotal();
    }

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "cid")
    private String cid;

    @JsonProperty(value = "org_name")
    private String orgName;

    @JsonProperty(value = "total")
    private Integer total;
}
