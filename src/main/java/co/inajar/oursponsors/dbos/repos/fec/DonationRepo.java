package co.inajar.oursponsors.dbos.repos.fec;

import co.inajar.oursponsors.dbos.entities.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DonationRepo extends JpaRepository<Donation, Long> {
    
}
