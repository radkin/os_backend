package co.inajar.oursponsors.models.propublica.congress;

import co.inajar.oursponsors.models.opensecrets.contributor.SmallContributorResponse;
import co.inajar.oursponsors.models.opensecrets.sector.SmallSectorResponse;
import co.inajar.oursponsors.models.user.PreferencesResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CongressDetailsResponse {

    @JsonProperty(value = "congress")
    private CongressResponse congress;
    @JsonProperty(value = "sectors")
    private List<SmallSectorResponse> sectors;
    @JsonProperty(value = "contributors")
    private List<SmallContributorResponse> contributors;
    @JsonProperty(value = "preferences")
    private PreferencesResponse preferences;

    public CongressDetailsResponse() {
    }

}
