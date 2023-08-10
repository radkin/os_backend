package co.inajar.oursponsors.dbos.entities.campaigns;

import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sponsors")
@Data
public class Sponsor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contribution_receipt_amount")
    private String contributionReceiptAmount;

    @Column(name = "contribution_receipt_date")
    private String contributionReceiptDate;

    @Column(name = "contributor_aggregate_ytd")
    private String contributorAggregateYtd;

    @Column(name = "contributor_city")
    private String contributorCity;

    @Column(name = "contributor_employer")
    private String contributorEmployer;

    @Column(name = "contributor_first_name")
    private String contributorFirstName;

    @Column(name = "contributor_last_name")
    private String contributorLastName;

    @Column(name = "contributor_middle_name")
    private String contributorMiddleName;

    @Column(name = "contributor_name")
    private String contributorName;

    @Column(name = "contributor_occupation")
    private String contributorOccupation;

    @Column(name = "contributor_state")
    private String contributorState;

    @Column(name = "contributor_street_1")
    private String contributorStreet1;

    @Column(name = "contributor_street_2")
    private String contributorStreet2;

    @Column(name = "contributor_zip")
    private String contributorZip;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "sponsor", orphanRemoval = true)
    private Set<Donation> donations = new HashSet<>();

    // ToDo: add a relationship to our donations table

    // ToDo: add a relationship to the recipient
}
