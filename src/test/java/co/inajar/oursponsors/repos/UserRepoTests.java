package co.inajar.oursponsors.repos;

import co.inajar.oursponsors.dbos.entities.User;
import co.inajar.oursponsors.dbos.repos.UserRepo;
import co.inajar.oursponsors.helpers.FiftyCharacterGenerator;
import co.inajar.oursponsors.helpers.NameGenerator;
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
    public void UserRepo_SaveAll_ReturnsSavedUser() {

        String token = TokenGenerator.generateSHAToken();
        String randomName = NameGenerator.generateRandomName();
        String[] name = randomName.split(" ");

        User user = User.builder()
                .firstName(name[0])
                .lastName(name[1])
                .apiKey(token)
                .build();

        User savedUser = userRepo.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void UserRepo_GetAll_ReturnsMoreThanOneUser() {

        String token1 = TokenGenerator.generateSHAToken();
        String randomName = NameGenerator.generateRandomName();
        String[] name = randomName.split(" ");

        String token2 = TokenGenerator.generateSHAToken();
        String randomName2 = NameGenerator.generateRandomName();
        String[] name2 = randomName2.split(" ");

        User user = User.builder()
                .firstName(name[0])
                .lastName(name[1])
                .apiKey(token1)
                .build();

        User user2 = User.builder()
                .firstName(name2[0])
                .lastName(name2[1])
                .apiKey(token2)
                .build();

        userRepo.save(user);
        userRepo.save(user2);

        List<User> userList = userRepo.findAll();

        Assertions.assertThat(userList).isNotNull();
        Assertions.assertThat(userList.size()).isGreaterThan(0);
    }

    @Test
    public void UserRepo_FindById_ReturnsUser() {

        String token = TokenGenerator.generateSHAToken();
        String randomName = NameGenerator.generateRandomName();
        String[] name = randomName.split(" ");

        User user = User.builder()
                .firstName(name[0])
                .lastName(name[1])
                .apiKey(token)
                .build();

        userRepo.save(user);

        User foundUser = userRepo.findById(user.getId()).get();

        Assertions.assertThat(foundUser).isNotNull();
    }

    @Test
    public void UserRepo_FindUserByApiKey_ReturnsUserUniqueResult() {

        String token = TokenGenerator.generateSHAToken();
        String randomName = NameGenerator.generateRandomName();
        String[] name = randomName.split(" ");

        User user = User.builder()
                .firstName(name[0])
                .lastName(name[1])
                .apiKey(token)
                .build();

        userRepo.save(user);

        User foundUser = userRepo.findUserByApiKey(user.getApiKey()).get();

        Assertions.assertThat(foundUser.getApiKey()).isEqualTo(user.getApiKey());
    }

    @Test
    public void UserRepo_FindUserByGoogleUid_ReturnsUserUniqueResult() {

        String token = FiftyCharacterGenerator.generateToken();
        String randomName = NameGenerator.generateRandomName();
        String[] name = randomName.split(" ");

        User user = User.builder()
                .firstName(name[0])
                .lastName(name[1])
                .apiKey(token)
                .build();

        userRepo.save(user);

        User foundUser = userRepo.findUserByApiKey(user.getApiKey()).get();

        Assertions.assertThat(foundUser.getApiKey()).isEqualTo(user.getApiKey());
    }
}