package co.inajar.oursponsors.repos;

import co.inajar.oursponsors.dbos.entities.User;
import co.inajar.oursponsors.dbos.repos.UserRepo;
import co.inajar.oursponsors.helpers.TokenGenerator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserRepoTests {

    @Autowired
    private UserRepo userRepo;

    @Test
    public void UserRepo_SaveAll_ReturnSavedUser() {

        String token = TokenGenerator.generateSHAToken();

        // arrange
        User user = User.builder()
                .firstName("fred")
                .lastName("flintstone")
                .apiKey(token)
                .build();


        // act

        User savedUser = userRepo.save(user);


        // assert

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void UserRepo_GetAll_ReturnMoreThanOneUser() {

        String token1 = TokenGenerator.generateSHAToken();
        String token2 = TokenGenerator.generateSHAToken();

        User user = User.builder()
                .firstName("fred")
                .lastName("flintstone")
                .apiKey(token1)
                .build();

        User user2 = User.builder()
                .firstName("fred")
                .lastName("flintstone")
                .apiKey(token2)
                .build();

        userRepo.save(user);
        userRepo.save(user2);

        List<User> userList = userRepo.findAll();

        Assertions.assertThat(userList).isNotNull();
        Assertions.assertThat(userList.size()).isGreaterThan(0);
    }

    @Test
    public void UserRepo_FindById_ReturnUser() {

        String token = TokenGenerator.generateSHAToken();

        User user = User.builder()
                .firstName("fred")
                .lastName("flintstone")
                .apiKey(token)
                .build();

        userRepo.save(user);

        User foundUser = userRepo.findById(user.getId()).get();

        Assertions.assertThat(foundUser).isNotNull();
    }

    @Test
    public void UserRepo_FindUserByApiKey_ReturnUser() {

        String token = TokenGenerator.generateSHAToken();

        User user = User.builder()
                .firstName("fred")
                .lastName("flintstone")
                .apiKey(token)
                .build();

        userRepo.save(user);

        User foundUser = userRepo.findUserByApiKey(user.getApiKey()).get();

        Assertions.assertThat(foundUser.getApiKey()).isEqualTo(user.getApiKey());
    }
}
