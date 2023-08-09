package co.inajar.oursponsors.models.fec;

import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class SponsorResponse {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "contribution_receipt_date")
    private String contributionReceiptDate;

    @JsonProperty(value = "contribution_receipt_amount")
    private String contributionReceiptAmount;

    @JsonProperty(value = "contributor_aggregate_ytd")
    private String contributorAggregateYtd;

    @JsonProperty(value = "contributor_city")
    private String contributorCity;

    @JsonProperty(value = "contributor_employer")
    private String contributorEmployer;

    @JsonProperty(value = "contributor_first_name")
    private String contributorFirstName;

    @JsonProperty(value = "contributor_last_name")
    private String contributorLastName;

    @JsonProperty(value = "contributor_middle_name")
    private String contributorMiddleName;

    @JsonProperty(value = "contributor_name")
    private String contributorName;

    @JsonProperty(value = "contributor_occupation")
    private String contributorOccupation;

    @JsonProperty(value = "contributor_state")
    private String contributorState;

    @JsonProperty(value = "contributor_street_1")
    private String contributorStreet1;

    @JsonProperty(value = "contributor_street_2")
    private String contributorStreet2;

    @JsonProperty(value = "contributor_zip")
    private String contributorZip;

    public SponsorResponse(Sponsor sponsor) {
        id = sponsor.getId();
        contributionReceiptDate = sponsor.getContributionReceiptDate();
        contributionReceiptAmount = sponsor.getContributionReceiptAmount();
        contributorAggregateYtd = sponsor.getContributorAggregateYtd();
        contributorCity = sponsor.getContributorCity();
        contributorEmployer = sponsor.getContributorEmployer();
        contributorFirstName = sponsor.getContributorFirstName();
        contributorLastName = sponsor.getContributorLastName();
        contributorMiddleName = sponsor.getContributorMiddleName();
        contributorName = sponsor.getContributorName();
        contributorOccupation = sponsor.getContributorOccupation();
        contributorState = sponsor.getContributorState();
        contributorStreet1 = sponsor.getContributorStreet1();
        contributorStreet2 = sponsor.getContributorStreet2();
        contributorZip = sponsor.getContributorZip();

    }
}
