package co.inajar.oursponsors.models.propublica;

import co.inajar.oursponsors.dbOs.entities.chambers.Congress;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class CongressResponse {

    private static final String BASE_URL = "https://theunitedstates.io/images/congress/original";

    public CongressResponse(Congress congress) {
        id = congress.getId();
        proPublicaId = congress.getProPublicaId();
        title = congress.getTitle();
        shortTitle = congress.getShortTitle();
        apiUri = congress.getApiUri();
        firstName = congress.getFirstName();
        middleName = congress.getMiddleName();
        lastName = congress.getLastName();
        suffix = congress.getSuffix();
        dateOfBirth = congress.getDateOfBirth();
        gender = congress.getGender();
        party = congress.getParty();
        leadershipRole = congress.getLeadershipRole();
        twitterAccount = congress.getTwitterAccount();
        facebookAccount = congress.getFacebookAccount();
        youtubeAccount = congress.getYoutubeAccount();
        govtrackId = congress.getGovtrackId();
        cspanId = congress.getCspanId();
        votesmartId = congress.getVotesmartId();
        icpsrId = congress.getIcpsrId();
        crpId = congress.getCrpId();
        googleEntityId = congress.getGoogleEntityId();
        fecCandidateId = congress.getFecCandidateId();
        url = congress.getUrl();
        rssUrl = congress.getRssUrl();
        contactForm = congress.getContactForm();
        inOffice = congress.getInOffice();
        dwNominate = congress.getDwNominate();
        seniority = congress.getSeniority();
        nextElection = congress.getNextElection();
        totalVotes = congress.getTotalVotes();
        missedVotes = congress.getMissedVotes();
        totalPresent = congress.getTotalPresent();
        lastUpdated = congress.getLastUpdated();
        ocdId = congress.getOcdId();
        office = congress.getOffice();
        phone = congress.getPhone();
        state = congress.getState();
        district = congress.getDistrict();
        atLarge = congress.getAtLarge();
        geoid = congress.getGeoid();
        missedVotesPct = congress.getMissedVotesPct();
        votesWithPartyPct = congress.getVotesWithPartyPct();
        votesAgainstPartyPct = congress.getVotesAgainstPartyPct();
        imageUrl = BASE_URL + "/" + congress.getProPublicaId() + ".jpg";
    }

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "pro_publica_id")
    private String proPublicaId;

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
    private LocalDate dateOfBirth;

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
    private LocalDateTime lastUpdated;

    @JsonProperty(value="ocd_id")
    private String ocdId;

    @JsonProperty(value="office")
    private String office;

    @JsonProperty(value="phone")
    private String phone;

//    @JsonProperty(value="fax")

    @JsonProperty(value="state")
    private String state;

    @JsonProperty(value="district")
    private String district;

    @JsonProperty(value="at_large")
    private Boolean atLarge;

    @JsonProperty(value="geoid")
    private String geoid;

    @JsonProperty(value="missed_votes_pct")
    private Double missedVotesPct;

    @JsonProperty(value="votes_with_party_pct")
    private Double votesWithPartyPct;

    @JsonProperty(value="votes_against_party_pct")
    private Double votesAgainstPartyPct;

    @JsonProperty(value="image_url")
    private String imageUrl;

}
