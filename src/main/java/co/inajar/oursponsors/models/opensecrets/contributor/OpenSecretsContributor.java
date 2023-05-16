package co.inajar.oursponsors.models.opensecrets.contributor;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenSecretsContributor {

    @JsonProperty(value = "org_name")
    private String orgName;

    @JsonProperty(value = "contributorid")
    private String contributorId;

    @JsonProperty(value = "total")
    private String total;

    @JsonProperty(value = "pacs")
    private String pacs;

    @JsonProperty(value = "indivs")
    private String indivs;

    @JsonProperty(value = "cid")
    private String cid;

    @JsonProperty(value = "cycle")
    private Integer cycle;
}
