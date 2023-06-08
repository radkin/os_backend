package co.inajar.oursponsors.dbos.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;


@Entity
@Table(name = "log_data")
public class Log {

    @Id
    private UUID id;

}
