package co.inajar.oursponsors.models.opensecrets.sector;

import co.inajar.oursponsors.dbos.entities.candidates.Sector;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SmallSectorResponse {

    @JsonProperty(value = "id")
    private Long id;
    @JsonProperty(value = "sector_name")
    private String sectorName;
    @JsonProperty(value = "total")
    private Integer total;
    @JsonProperty(value = "cid")
    private String cid;

    public SmallSectorResponse(Sector sector) {
        id = sector.getId();
        sectorName = sector.getSectorName();
        total = sector.getTotal();
        cid = sector.getCid();
    }
}
