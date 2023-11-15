package co.inajar.oursponsors.dbos.entities.candidates;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sectors")
@Data
public class Sector {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cid")
    private String cid;

    @Column(name = "cycle")
    private Integer cycle;

    @Column(name = "sector_name")
    private String sectorName;

    @Column(name = "sectorid")
    private String sectorId;

    @Column(name = "indivs")
    private Integer indivs;

    @Column(name = "pacs")
    private Integer pacs;

    @Column(name = "total")
    private Integer total;
}
