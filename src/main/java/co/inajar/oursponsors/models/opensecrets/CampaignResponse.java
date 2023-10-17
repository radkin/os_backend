package co.inajar.oursponsors.models.opensecrets;

import co.inajar.oursponsors.models.fec.MiniDonationResponse;
import co.inajar.oursponsors.models.fec.SponsorResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class CampaignResponse {
    @JsonProperty("donations")
    private List<MiniDonationResponse> donations;

    @JsonProperty("committees")
    private List<CommitteeResponse> committees;

    @JsonProperty("sponsors")
    private List<SponsorResponse> sponsors;
}

