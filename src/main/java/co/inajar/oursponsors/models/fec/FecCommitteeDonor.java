package co.inajar.oursponsors.models.fec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FecCommitteeDonor {

    @JsonProperty(value = "contribution_receipt_date")
    private String contributionReceiptDate;

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

}
