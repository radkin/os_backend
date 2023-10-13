package co.inajar.oursponsors.services.user;

import co.inajar.oursponsors.dbos.entities.User;
import co.inajar.oursponsors.dbos.repos.UserRepo;
import co.inajar.oursponsors.helpers.NameGenerator;
import co.inajar.oursponsors.helpers.TokenGenerator;
import co.inajar.oursponsors.models.user.UserRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private UserManagerImpl userManager;

    @Test
    public void UserManager_CreateAndUpdateUser_ReturnsUserThenUpdates() {
        String token = TokenGenerator.generateSHAToken();
        String randomName = NameGenerator.generateRandomName();
        String[] name = randomName.split(" ");

        // existing data
        User user = User.builder()
                .firstName(name[0])
                .lastName(name[1])
                .apiKey(token)
                .build();

        String randomName2 = NameGenerator.generateRandomName();
        String[] name2 = randomName2.split(" ");

        // new data in the request
        UserRequest userRequest = UserRequest.builder()
                .firstName(name2[0])
                .lastName(name2[1])
                .party("R")
                .state("OH")
                .build();

        // create user, Note: we create and "add to" the existing user
        when(userRepo.save(Mockito.any(User.class))).thenReturn(user);
        User savedUser = userManager.createOrUpdateUser(userRequest, user);
        Assertions.assertThat(savedUser).isNotNull();

        // update user, here we are updating an existing value
        savedUser.setState("NM");
        Assertions.assertThat(savedUser.getState()).isEqualTo("NM");

    }


}
