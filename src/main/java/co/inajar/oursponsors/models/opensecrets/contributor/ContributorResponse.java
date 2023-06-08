package co.inajar.oursponsors.models.opensecrets.contributor;

import co.inajar.oursponsors.dbos.entities.candidates.Contributor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ContributorResponse {

    @JsonProperty(value = "id")
    private Long id;
    @JsonProperty(value = "cid")
    private String cid;
    @JsonProperty(value = "cycle")
    private Integer cycle;
    @JsonProperty(value = "org_name")
    private String orgName;
    @JsonProperty(value = "total")
    private Integer total;
    @JsonProperty(value = "pacs")
    private Integer pacs;
    @JsonProperty(value = "indivs")
    private Integer indivs;

    public ContributorResponse(Contributor contributor) {
        id = contributor.getId();
        cid = contributor.getCid();
        cycle = contributor.getCycle();
        orgName = contributor.getOrgName();
        total = contributor.getTotal();
        pacs = contributor.getPacs();
        indivs = contributor.getIndivs();
    }
}
