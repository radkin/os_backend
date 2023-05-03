package co.inajar.oursponsors.models.opensecrets.sector;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Year;

@Data
public class SectorResponse {

    public SectorResponse() {}

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

    @JsonProperty(value = "cycle")
    private Year cycle;

    @JsonProperty(value = "cid")
    private String cid;
}
