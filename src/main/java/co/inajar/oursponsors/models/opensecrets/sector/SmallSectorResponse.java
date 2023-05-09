package co.inajar.oursponsors.models.opensecrets.sector;

import co.inajar.oursponsors.dbOs.entities.candidates.Sector;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SmallSectorResponse {

    public SmallSectorResponse(Sector sector) {
        id = sector.getId();
        sectorName = sector.getSectorName();
        total = sector.getTotal();
        cid = sector.getCid();
    }
    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "sector_name")
    private String sectorName;

    @JsonProperty(value = "total")
    private Integer total;

    @JsonProperty(value = "cid")
    private String cid;
}
