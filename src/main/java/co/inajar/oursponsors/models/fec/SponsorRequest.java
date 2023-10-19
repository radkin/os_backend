package co.inajar.oursponsors.models.fec;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class SponsorRequest {

    // senator or congress
    @JsonAlias(value = "chamber")
    private String chamber;

    // Senator or Congress ID
    @JsonAlias(value = "os_id")
    private Long osId;

}
