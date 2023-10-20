package co.inajar.oursponsors.dbos.entities.campaigns;

import lombok.Data;

import javax.persistence.*;

@Entity
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
