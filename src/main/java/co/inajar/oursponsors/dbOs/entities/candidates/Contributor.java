package co.inajar.oursponsors.dbOs.entities.candidates;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="contributors")
@Data
public class Contributor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="cid")
    private String cid;

    @Column(name="org_name")
    private String orgName;

    @Column(name="total")
    private Integer total;

    @Column(name="pacs")
    private Integer pacs;

    @Column(name="indivs")
    private Integer indivs;
}
