package co.inajar.oursponsors.models.opensecrets;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class CommitteeRequest {

    @JsonAlias(value = "two_year_transaction_period")
    private Integer twoYearTransactionPeriod;

    // pro publica ID
    @JsonAlias(value = "pp_id")
    private String ppId;

    // pro public crp_id / FEC committee_id
    @JsonAlias(value = "crp_id")
    private String crpId;

}
