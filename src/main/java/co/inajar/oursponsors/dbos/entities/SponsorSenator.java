package co.inajar.oursponsors.dbos.entities;

import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "sponsor_senators")
@Data
public class SponsorSenator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sponsor_id")
    private Sponsor sponsor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senator_id")
    private Senator senator;

}
