package co.inajar.oursponsors.dbos.entities;

import co.inajar.oursponsors.dbos.entities.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "user_preferences")
@Data
public class Preferences {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "my_state_only")
    private Boolean myStateOnly;
    @Column(name = "my_party_only")
    private Boolean myPartyOnly;
    @Column(name = "my_county_only")
    private Boolean myCountyOnly;
    @Column(name = "twitter_hide")
    private Boolean twitterHide;
    @Column(name = "facebook_hide")
    private Boolean facebookHide;
    @Column(name = "youtube_hide")
    private Boolean youtubeHide;
    @Column(name = "google_entity_hide")
    private Boolean googleEntityHide;
    @Column(name = "cspan_hide")
    private Boolean cspanHide;
    @Column(name = "vote_smart_hide")
    private Boolean voteSmartHide;
    @Column(name = "gov_track_hide")
    private Boolean govTrackHide;
    @Column(name = "open_secrets_hide")
    private Boolean openSecretsHide;
    @Column(name = "vote_view_hide")
    private Boolean voteViewHide;
    @Column(name = "fec_hide")
    private Boolean fecHide;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
