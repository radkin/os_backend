package co.inajar.oursponsors.repos;

import co.inajar.oursponsors.dbos.entities.User;
import co.inajar.oursponsors.dbos.repos.UserRepo;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserRepoTests {

    @Autowired
    private UserRepo userRepo;

    @Test
    public void UserRepo_SaveAll_ReturnSavedRestApiKey() {

        // arrange
        User user = User.builder()
                .firstName("fred")
                .lastName("flintstone")
                .apiKey("yippie")
                .build();


        // act

        User savedUser = userRepo.save(user);


        // assert

        Assertions.assertThat(savedUser).isNotNull();
        

    }
}
