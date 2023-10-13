package co.inajar.oursponsors.dbos.entities.campaigns;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "committees")
@Data
public class Committee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fec_committee_id")
    private String fecCommitteeId;

    @Column(name = "two_year_transaction_period")
    private Integer twoYearTransactionPeriod;

    // could be Senator or Congress member
    @Column(name = "pp_id")
    private String ppId;
}
