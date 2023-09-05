package co.inajar.oursponsors.models.fec;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class FecCommitteeRequest {
    // senator or congress
    @JsonAlias(value = "chamber")
    private String chamber;

    // our sponsors ID for the above
    @JsonAlias(value = "os_id")
    private Long osId;
}
