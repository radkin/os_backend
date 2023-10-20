package co.inajar.oursponsors.models.user;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class PreferencesRequest {

    @JsonAlias(value = "id")
    private Long id;

    @JsonAlias(value = "user_id")
    private Long userId;

    @JsonAlias(value = "my_state_only")
    private Boolean myStateOnly;

    @JsonAlias(value = "my_party_only")
    private Boolean myPartyOnly;

    @JsonAlias(value = "my_county_only")
    private Boolean myCountyOnly;

    @JsonAlias(value = "twitter_hide")
    private Boolean twitterHide;

    @JsonAlias(value = "facebook_hide")
    private Boolean facebookHide;

    @JsonAlias(value = "youtube_hide")
    private Boolean youtubehide;

    @JsonAlias(value = "google_entity_hide")
    private Boolean googleEntityHide;

    @JsonAlias(value = "cspan_hide")
    private Boolean cspanHide;

    @JsonAlias(value = "vote_smart_hide")
    private Boolean voteSmartHide;

    @JsonAlias(value = "gov_track_hide")
    private Boolean govTrackHide;

    @JsonAlias(value = "open_secrets_hide")
    private Boolean openSecretsHide;

    @JsonAlias(value = "vote_view_hide")
    private Boolean voteViewHide;

    @JsonAlias(value = "fec_hide")
    private Boolean fecHide;

}
