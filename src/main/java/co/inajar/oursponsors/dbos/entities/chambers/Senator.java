package co.inajar.oursponsors.dbos.entities.chambers;

import co.inajar.oursponsors.dbos.entities.campaigns.Sponsor;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "senators")
@Data
public class Senator extends Rep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "senate_class")
    private Integer senateClass;

    @Column(name = "state_rank")
    private String stateRank;

    @Column(name = "lis_id", unique = true)
    private String lisId;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "senator", orphanRemoval = true)
    private Set<Sponsor> sponsors = new HashSet<>();

}
