package co.inajar.oursponsors.dbos.entities;

import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "sponsor_congress")
@Data
public class SponsorCongress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sponsor_id")
    private Sponsor sponsor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Congress_id")
    private Congress congress;

}
