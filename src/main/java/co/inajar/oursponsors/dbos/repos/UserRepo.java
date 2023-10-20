package co.inajar.oursponsors.dbos.repos;

import co.inajar.oursponsors.dbos.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findUserByApiKey(String apiKey);

    Optional<User> findById(Long userId);

    List<User> findAllByIdIn(List<Long> ids);

    Optional<User> findUserByGoogleUid(String googleUid);
}
