package co.inajar.oursponsors.models.opensecrets.sector;

import co.inajar.oursponsors.dbOs.entities.candidates.Sector;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SectorResponse {

    public SectorResponse(Sector sector) {
        id = sector.getId();
        sectorName = sector.getSectorName();
        sectorId = sector.getSectorId();
        indivs = sector.getIndivs();
        pacs = sector.getPacs();
        total = sector.getTotal();
        cycle = sector.getCycle();
        cid = sector.getCid();
    }
    @JsonProperty(value = "id")
    private Long id;

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
    private Integer cycle;

    @JsonProperty(value = "cid")
    private String cid;
}
