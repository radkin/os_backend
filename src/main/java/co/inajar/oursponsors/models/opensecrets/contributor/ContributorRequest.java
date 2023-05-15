package co.inajar.oursponsors.models.opensecrets.contributor;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class ContributorRequest {

    @JsonAlias(value = "id")
    private Long id;

    @JsonAlias(value = "org_name")
    private String sectorName;

    @JsonAlias(value = "cid")
    private String cid;
}
