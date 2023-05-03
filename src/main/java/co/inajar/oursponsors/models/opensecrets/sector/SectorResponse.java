package co.inajar.oursponsors.models.opensecrets.sector;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SectorResponse {

    @JsonProperty(value = "sector_name")
    private String sectorName;

    @JsonProperty(value = "sectorid")
    private String sectorId;

    @JsonProperty(value = "indivs")
    private Integer indivs;

    @JsonProperty(value = "pacs")
    private Integer pacs;

    @JsonProperty(value = "total")
    private Integer total;
}
