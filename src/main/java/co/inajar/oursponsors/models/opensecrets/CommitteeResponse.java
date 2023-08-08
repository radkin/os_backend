package co.inajar.oursponsors.models.opensecrets;

import co.inajar.oursponsors.dbos.entities.Committee;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CommitteeResponse {

    @JsonProperty(value = "id")
    private Long id;

    @JsonProperty(value = "fec_committee_id")
    private String fecCommitteeId;

    @JsonProperty(value = "two_year_transaction_period")
    private Integer twoYearTransactionPeriod;

    @JsonProperty(value = "pp_id")
    private String ppId;

    public CommitteeResponse(Committee committee) {
        id = committee.getId();
        fecCommitteeId = committee.getFecCommitteeId();
        twoYearTransactionPeriod = committee.getTwoYearTransactionPeriod();
        ppId = committee.getPpId();
    }
}
