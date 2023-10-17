package co.inajar.oursponsors.models.propublica.senator;

import co.inajar.oursponsors.models.opensecrets.contributor.SmallContributorResponse;
import co.inajar.oursponsors.models.opensecrets.sector.SmallSectorResponse;
import co.inajar.oursponsors.models.user.PreferencesResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SenatorDetailsResponse {
    @JsonProperty("senator")
    private SenatorResponse senator;

    @JsonProperty("sectors")
    private List<SmallSectorResponse> sectors;

    @JsonProperty("contributors")
    private List<SmallContributorResponse> contributors;

    @JsonProperty("preferences")
    private PreferencesResponse preferences;
}
