package co.inajar.oursponsors.dbos.entities;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "donations")
@Data
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "date_of_donation")
    private LocalDate dateOfDonation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sponsor_id")
    private Sponsor sponsor;

    // could be Senator or Congress member
    @Column(name = "pp_id")
    private String ppId;
}
