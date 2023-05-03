package co.inajar.oursponsors.models.opensecrets.sector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Year;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenSecretsSector {

    @JsonProperty(value = "sector_name")
    private String sectorName;

    @JsonProperty(value = "sectorid")
    private String sectorId;

    @JsonProperty(value = "indivs")
    private String indivs;

    @JsonProperty(value = "pacs")
    private String pacs;

    @JsonProperty(value = "total")
    private String total;

    @JsonProperty(value = "cycle")
    private Year cycle;

    @JsonProperty(value = "cid")
    private String cid;

}
