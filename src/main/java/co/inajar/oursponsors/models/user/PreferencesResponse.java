package co.inajar.oursponsors.models.user;

import co.inajar.oursponsors.dbOs.entities.user.Preferences;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PreferencesResponse {
    public PreferencesResponse() {}

    public PreferencesResponse(Preferences preferences) {
        id = preferences.getId();
        userId = preferences.getUser().getId();
        myStateOnly = preferences.getMyStateOnly();
        myPartyOnly = preferences.getMyPartyOnly();
        myCountyOnly = preferences.getMyCountyOnly();
        twitterHide = preferences.getTwitterHide();
        facebookHide = preferences.getFacebookHide();
        youtubehide = preferences.getYoutubeHide();
        googleEntityHide = preferences.getGoogleEntityHide();
        cspanHide = preferences.getCspanHide();
        voteSmartHide = preferences.getVoteSmartHide();
        govTrackHide = preferences.getGovTrackHide();
        openSecretsHide = preferences.getOpenSecretsHide();
        voteViewHide = preferences.getVoteViewHide();
        fecHide = preferences.getFecHide();
    }
    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "user_id")
    private Long userId;

    @JsonProperty(value = "my_state_only")
    private Boolean myStateOnly;

    @JsonProperty(value = "my_party_only")
    private Boolean myPartyOnly;

    @JsonProperty(value = "my_county_only")
    private Boolean myCountyOnly;

    @JsonProperty(value = "twitter_hide")
    private Boolean twitterHide;

    @JsonProperty(value = "facebook_hide")
    private Boolean facebookHide;

    @JsonProperty(value = "youtube_hide")
    private Boolean youtubehide;

    @JsonProperty(value = "google_entity_hide")
    private Boolean googleEntityHide;

    @JsonProperty(value = "cspan_hide")
    private Boolean cspanHide;

    @JsonProperty(value = "vote_smart_hide")
    private Boolean voteSmartHide;

    @JsonProperty(value = "gov_track_hide")
    private Boolean govTrackHide;

    @JsonProperty(value = "open_secrets_hide")
    private Boolean openSecretsHide;

    @JsonProperty(value = "vote_view_hide")
    private Boolean voteViewHide;

    @JsonProperty(value = "fec_hide")
    private Boolean fecHide;
}
