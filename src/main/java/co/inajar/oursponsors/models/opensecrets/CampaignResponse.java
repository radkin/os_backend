package co.inajar.oursponsors.models.opensecrets;

import co.inajar.oursponsors.models.fec.SponsorResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CampaignResponse {

//    @JsonProperty(value = "donations")
//    private List<DonationResponse> donations;

    @JsonProperty(value = "committees")
    private List<CommitteeResponse> committees;

    @JsonProperty(value = "sponsors")
    private List<SponsorResponse> sponsors;

    public CampaignResponse() {
    }
}
