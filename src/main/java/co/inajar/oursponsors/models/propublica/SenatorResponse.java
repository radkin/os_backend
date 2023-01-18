package co.inajar.oursponsors.models.propublica;

import co.inajar.oursponsors.dbOs.entities.chamber.senate.Senator;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SenatorResponse {

    public SenatorResponse(Senator senator) {
        id = senator.getId();
        title = senator.getTitle();
        shortTitle = senator.getShortTitle();
        apiUri = senator.getApiUri();
        firstName = senator.getFirstName();
        middleName = senator.getMiddleName();
        lastName = senator.getLastName();
        suffix = senator.getSuffix();
        dateOfBirth = senator.getDateOfBirth();
        gender = senator.getGender();
        party = senator.getParty();
        leadershipRole = senator.getLeadershipRole();
        twitterAccount = senator.getTwitterAccount();
        facebookAccount = senator.getFacebookAccount();
        youtubeAccount = senator.getYoutubeAccount();
        govtrackId = senator.getGovtrackId();
        votesmartId = senator.getVotesmartId();
        icpsrId = senator.getIcpsrId();
        crpId = senator.getCrpId();
        googleEntityId = senator.getGoogleEntityId();
        fecCandidateId = senator.getFecCandidateId();
        url = senator.getUrl();
        rssUrl = senator.getRssUrl();
        contactForm = senator.getContactForm();
        inOffice = senator.getInOffice();
        dwNominate = senator.getDwNominate();
        seniority = senator.getSeniority();
        nextElection = senator.getNextElection();
        totalVotes = senator.getTotalVotes();
        missedVotes = senator.getMissedVotes();
        totalPresent = senator.getTotalPresent();
        lastUpdated = senator.getLastUpdated();
        ocdId = senator.getOcdId();
        office = senator.getOffice();
        phone = senator.getPhone();
        state = senator.getState();
        senateClass = senator.getSenateClass();
        stateRank = senator.getStateRank();
        lisId = senator.getLisId();
        missedVotesPct = senator.getMissedVotesPct();
        votesWithPartyPct = senator.getVotesWithPartyPct();
        votesAgainstPartyPct = senator.getVotesAgainstPartyPct();
    }

    @JsonAlias(value = "id")
    private Long id;
    public void setId(Long theId) { id = theId; }
    public Long getId() { return id; }

    @JsonAlias(value="title")
    private String title;

    @JsonAlias(value="short_title")
    private String shortTitle;

    @JsonAlias(value="api_uri")
    private String apiUri;

    @JsonAlias(value="first_name")
    private String firstName;

    @JsonAlias(value="middle_name")
    private String middleName;

    @JsonAlias(value="last_name")
    private String lastName;

    @JsonAlias(value="suffix")
    private String suffix;

    @JsonAlias(value="date_of_birth")
    private LocalDate dateOfBirth;

    @JsonAlias(value="gender")
    private String gender;

    @JsonAlias(value="party")
    private String party;

    @JsonAlias(value="leadership_role")
    private String leadershipRole;

    @JsonAlias(value="twitter_account")
    private String twitterAccount;

    @JsonAlias(value="facebook_account")
    private String facebookAccount;

    @JsonAlias(value="youtube_account")
    private String youtubeAccount;

    @JsonAlias(value="govtrack_id")
    private Long govtrackId;

    @JsonAlias(value="cspan_id")
    private Long cspanId;

    @JsonAlias(value="votesmart_id")
    private Long votesmartId;

    @JsonAlias(value="icpsr_id")
    private Long icpsrId;

    @JsonAlias(value="crp_id")
    private String crpId;

    @JsonAlias(value="google_entity_id")
    private String googleEntityId;

    @JsonAlias(value="fec_candidate_id")
    private String fecCandidateId;

    @JsonAlias(value="url")
    private String url;

    @JsonAlias(value="rss_url")
    private String rssUrl;

    @JsonAlias(value="contact_form")
    private String contactForm;

    @JsonAlias(value="in_office")
    private Boolean inOffice;

    // This is always null
    //    @JsonAlias(value="cook_pvi")

    @JsonAlias(value="dw_nominate")
    private Double dwNominate;

    // this is always null
    //    @JsonAlias(value="ideal_point")

    @JsonAlias(value="seniority")
    private Integer seniority;

    @JsonAlias(value="next_election")
    private LocalDate nextElection;

    @JsonAlias(value="total_votes")
    private Integer totalVotes;

    @JsonAlias(value="missed_votes")
    private Integer missedVotes;

    @JsonAlias(value="total_present")
    private Integer totalPresent;

    @JsonAlias(value="last_updated")
    private LocalDateTime lastUpdated;

    @JsonAlias(value="ocd_id")
    private String ocdId;

    @JsonAlias(value="office")
    private String office;

    @JsonAlias(value="phone")
    private String phone;

//    @JsonAlias(value="fax")

    @JsonAlias(value="state")
    private String state;

    @JsonAlias(value="senate_class")
    private Integer senateClass;

    @JsonAlias(value="state_rank")
    private String stateRank;

    @JsonAlias(value="lis_id")
    private String lisId;

    @JsonAlias(value="missed_votes_pct")
    private Double missedVotesPct;

    @JsonAlias(value="votes_with_party_pct")
    private Double votesWithPartyPct;

    @JsonAlias(value="votes_against_party_pct")
    private Double votesAgainstPartyPct;
    
}
