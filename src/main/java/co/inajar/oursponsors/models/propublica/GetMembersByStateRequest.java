package co.inajar.oursponsors.models.propublica;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class GetMembersByStateRequest {

    @JsonAlias(value = "state")
    private String state;
}
