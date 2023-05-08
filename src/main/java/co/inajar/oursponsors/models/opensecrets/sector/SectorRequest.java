package co.inajar.oursponsors.models.opensecrets.sector;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class SectorRequest {

    @JsonAlias(value = "id")
    private Long id;

    @JsonAlias(value = "sector_name")
    private String sectorName;

    @JsonAlias(value = "cid")
    private String cid;
}
