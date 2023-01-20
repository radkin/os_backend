package co.inajar.oursponsors.models.propublica;

import co.inajar.oursponsors.dbOs.entities.chamber.senate.Senator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProPublicaSenator {

    @JsonProperty(value = "id")
    private String id;

    @JsonProperty(value="title")
    private String title;

    @JsonProperty(value="short_title")
    private String shortTitle;

    @JsonProperty(value="api_uri")
    private String apiUri;

    @JsonProperty(value="first_name")
    private String firstName;

    @JsonProperty(value="middle_name")
    private String middleName;

    @JsonProperty(value="last_name")
    private String lastName;

    @JsonProperty(value="suffix")
    private String suffix;

    @JsonProperty(value="date_of_birth")
    private String dateOfBirth;

    @JsonProperty(value="gender")
    private String gender;

    @JsonProperty(value="party")
    private String party;

    @JsonProperty(value="leadership_role")
    private String leadershipRole;

    @JsonProperty(value="twitter_account")
    private String twitterAccount;

    @JsonProperty(value="facebook_account")
    private String facebookAccount;

    @JsonProperty(value="youtube_account")
    private String youtubeAccount;

    @JsonProperty(value="govtrack_id")
    private Long govtrackId;

    @JsonProperty(value="cspan_id")
    private Long cspanId;

    @JsonProperty(value="votesmart_id")
    private Long votesmartId;

    @JsonProperty(value="icpsr_id")
    private Long icpsrId;

    @JsonProperty(value="crp_id")
    private String crpId;

    @JsonProperty(value="google_entity_id")
    private String googleEntityId;

    @JsonProperty(value="fec_candidate_id")
    private String fecCandidateId;

    @JsonProperty(value="url")
    private String url;

    @JsonProperty(value="rss_url")
    private String rssUrl;

    @JsonProperty(value="contact_form")
    private String contactForm;

    @JsonProperty(value="in_office")
    private Boolean inOffice;

    // This is always null
    //    @JsonProperty(value="cook_pvi")

    @JsonProperty(value="dw_nominate")
    private Double dwNominate;

    // this is always null
    //    @JsonProperty(value="ideal_point")

    @JsonProperty(value="seniority")
    private Integer seniority;

    @JsonProperty(value="next_election")
    private String nextElection;

    @JsonProperty(value="total_votes")
    private Integer totalVotes;

    @JsonProperty(value="missed_votes")
    private Integer missedVotes;

    @JsonProperty(value="total_present")
    private Integer totalPresent;

    @JsonProperty(value="last_updated")
    private String lastUpdated;

    @JsonProperty(value="ocd_id")
    private String ocdId;

    @JsonProperty(value="office")
    private String office;

    @JsonProperty(value="phone")
    private String phone;

//    @JsonProperty(value="fax")

    @JsonProperty(value="state")
    private String state;

    @JsonProperty(value="senate_class")
    private Integer senateClass;

    @JsonProperty(value="state_rank")
    private String stateRank;

    @JsonProperty(value="lis_id")
    private String lisId;

    @JsonProperty(value="missed_votes_pct")
    private Double missedVotesPct;

    @JsonProperty(value="votes_with_party_pct")
    private Double votesWithPartyPct;

    @JsonProperty(value="votes_against_party_pct")
    private Double votesAgainstPartyPct;
    
}
