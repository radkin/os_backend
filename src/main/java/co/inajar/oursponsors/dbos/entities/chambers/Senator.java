package co.inajar.oursponsors.dbos.entities.chambers;

import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "senators")
@Data
public class Senator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pp_id", unique = true)
    private String proPublicaId;

    @Column(name = "title")
    private String title;

    @Column(name = "short_title")
    private String shortTitle;

    @Column(name = "api_uri")
    private String apiUri;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "suffix")
    private String suffix;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "party")
    private String party;

    @Column(name = "leadership_role")
    private String leadershipRole;

    @Column(name = "twitter_account")
    private String twitterAccount;

    @Column(name = "facebook_account")
    private String facebookAccount;

    @Column(name = "youtube_account")
    private String youtubeAccount;

    @Column(name = "govtrack_id", unique = true)
    private Long govtrackId;

    @Column(name = "cspan_id", unique = true)
    private Long cspanId;

    @Column(name = "votesmart_id", unique = true)
    private Long votesmartId;

    @Column(name = "icpsr_id", unique = true)
    private Long icpsrId;

    @Column(name = "crp_id", unique = true)
    private String crpId;

    @Column(name = "google_entity_id", unique = true)
    private String googleEntityId;

    @Column(name = "fec_candidate_id", unique = true)
    private String fecCandidateId;

    @Column(name = "url")
    private String url;

    @Column(name = "rss_url")
    private String rssUrl;

    @Column(name = "contact_form")
    private String contactForm;

    @Column(name = "in_office")
    private Boolean inOffice;

    // This is always null
    //    @Column(name="cook_pvi")

    @Column(name = "dw_nominate")
    private Double dwNominate;

    // this is always null
    //    @Column(name="ideal_point")

    @Column(name = "seniority")
    private Integer seniority;

    @Column(name = "next_election")
    private String nextElection;

    @Column(name = "total_votes")
    private Integer totalVotes;

    @Column(name = "missed_votes")
    private Integer missedVotes;

    @Column(name = "total_present")
    private Integer totalPresent;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Column(name = "ocd_id", unique = true)
    private String ocdId;

    @Column(name = "office")
    private String office;

    @Column(name = "phone")
    private String phone;

//    @Column(name="fax")

    @Column(name = "state")
    private String state;

    @Column(name = "senate_class")
    private Integer senateClass;

    @Column(name = "state_rank")
    private String stateRank;

    @Column(name = "lis_id", unique = true)
    private String lisId;

    @Column(name = "missed_votes_pct")
    private Double missedVotesPct;

    @Column(name = "votes_with_party_pct")
    private Double votesWithPartyPct;

    @Column(name = "votes_against_party_pct")
    private Double votesAgainstPartyPct;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "senator", orphanRemoval = true)
    private Set<Sponsor> sponsors = new HashSet<>();

}
