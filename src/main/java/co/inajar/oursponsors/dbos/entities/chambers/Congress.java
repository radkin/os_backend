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
@Table(name = "congress")
@Data
public class Congress extends Rep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "district")
    private String district;

    @Column(name = "at_large")
    private Boolean atLarge;

    @Column(name = "geoid")
    private String geoid;

    @OneToMany(cascade = CascadeType.ALL,
            mappedBy = "congress", orphanRemoval = true)
    private Set<Sponsor> sponsors = new HashSet<>();


}
