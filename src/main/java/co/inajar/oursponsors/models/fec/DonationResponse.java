package co.inajar.oursponsors.models.fec;

import co.inajar.oursponsors.dbos.entities.campaigns.Donation;
import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class DonationResponse {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "amount")
    private String amount;

    @JsonProperty(value = "date_of_donation")
    private LocalDate dateOfDonation;

    @JsonProperty(value = "sponsor")
    private Sponsor sponsor;

    @JsonProperty(value = "pp_id")
    private String ppId;

    public DonationResponse(Donation donation) {
        id = donation.getId();
        amount = String.valueOf(donation.getAmount());
        dateOfDonation = donation.getDateOfDonation();
//        sponsor = donation.getSponsor();
        ppId = donation.getPpId();
    }

}
