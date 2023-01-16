package co.inajar.oursponsors.dbOs.entities;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name = "log_data")
public class Log {

    @Id
    private UUID id;

}
