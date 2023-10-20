package co.inajar.oursponsors.models.fec;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class SponsorRequest {

    // senator or congress
    @JsonAlias(value = "chamber")
    private String chamber;

    // Senator or Congress ID
    @JsonAlias(value = "os_id")
    private Long osId;

}
