package co.inajar.oursponsors.dbOs.repos;

import co.inajar.oursponsors.dbOs.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByApiKey(String apiKey);
    Optional<User> findById(Long userId);
    List<User> findAllByIdIn(List<Long> ids);
    Optional<User> findUserByEmail(String email);
}
