package co.inajar.oursponsors.models.fec;

import co.inajar.oursponsors.dbos.entities.campaigns.Donation;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class MiniDonationResponse {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "amount")
    private String amount;

    @JsonProperty(value = "date_of_donation")
    private LocalDate dateOfDonation;

    @JsonProperty(value = "pp_id")
    private String ppId;

    public MiniDonationResponse(Donation donation) {
        id = donation.getId();
        amount = String.valueOf(donation.getAmount());
        dateOfDonation = donation.getDateOfDonation();
        ppId = donation.getPpId();
    }

}
