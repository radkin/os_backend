package co.inajar.oursponsors.models.propublica.senator;

import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SenatorResponse {

    private static final String BASE_URL = "https://theunitedstates.io/images/congress/original";
    @JsonProperty(value = "id")
    private Long id;
    @JsonProperty(value = "pro_publica_id")
    private String proPublicaId;
    @JsonProperty(value = "title")
    private String title;
    @JsonProperty(value = "short_title")
    private String shortTitle;
    @JsonProperty(value = "api_uri")
    private String apiUri;
    @JsonProperty(value = "first_name")
    private String firstName;
    @JsonProperty(value = "middle_name")
    private String middleName;
    @JsonProperty(value = "last_name")
    private String lastName;
    @JsonProperty(value = "suffix")
    private String suffix;
    @JsonProperty(value = "date_of_birth")
    private LocalDate dateOfBirth;
    @JsonProperty(value = "gender")
    private String gender;
    @JsonProperty(value = "party")
    private String party;
    @JsonProperty(value = "leadership_role")
    private String leadershipRole;
    @JsonProperty(value = "twitter_account")
    private String twitterAccount;
    @JsonProperty(value = "facebook_account")
    private String facebookAccount;
    @JsonProperty(value = "youtube_account")
    private String youtubeAccount;
    @JsonProperty(value = "govtrack_id")
    private Long govtrackId;
    @JsonProperty(value = "cspan_id")
    private Long cspanId;
    @JsonProperty(value = "votesmart_id")
    private Long votesmartId;
    @JsonProperty(value = "icpsr_id")
    private Long icpsrId;
    @JsonProperty(value = "crp_id")
    private String crpId;
    @JsonProperty(value = "google_entity_id")
    private String googleEntityId;
    @JsonProperty(value = "fec_candidate_id")
    private String fecCandidateId;
    @JsonProperty(value = "url")
    private String url;
    @JsonProperty(value = "rss_url")
    private String rssUrl;
    @JsonProperty(value = "contact_form")
    private String contactForm;
    @JsonProperty(value = "in_office")
    private Boolean inOffice;
    @JsonProperty(value = "dw_nominate")
    private Double dwNominate;

    // This is always null
    //    @JsonProperty(value="cook_pvi")
    @JsonProperty(value = "seniority")
    private Integer seniority;

    // this is always null
    //    @JsonProperty(value="ideal_point")
    @JsonProperty(value = "next_election")
    private String nextElection;
    @JsonProperty(value = "total_votes")
    private Integer totalVotes;
    @JsonProperty(value = "missed_votes")
    private Integer missedVotes;
    @JsonProperty(value = "total_present")
    private Integer totalPresent;
    @JsonProperty(value = "last_updated")
    private LocalDateTime lastUpdated;
    @JsonProperty(value = "ocd_id")
    private String ocdId;
    @JsonProperty(value = "office")
    private String office;
    @JsonProperty(value = "phone")
    private String phone;
    @JsonProperty(value = "state")
    private String state;

//    @JsonProperty(value="fax")
    @JsonProperty(value = "senate_class")
    private Integer senateClass;
    @JsonProperty(value = "state_rank")
    private String stateRank;
    @JsonProperty(value = "lis_id")
    private String lisId;
    @JsonProperty(value = "missed_votes_pct")
    private Double missedVotesPct;
    @JsonProperty(value = "votes_with_party_pct")
    private Double votesWithPartyPct;
    @JsonProperty(value = "votes_against_party_pct")
    private Double votesAgainstPartyPct;
    @JsonProperty(value = "image_url")
    private String imageUrl;

    public SenatorResponse(Senator senator) {
        id = senator.getId();
        proPublicaId = senator.getProPublicaId();
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
        cspanId = senator.getCspanId();
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
        imageUrl = BASE_URL + "/" + senator.getProPublicaId() + ".jpg";
    }

}
