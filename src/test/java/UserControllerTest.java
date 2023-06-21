import co.inajar.oursponsors.controllers.user.UserController;
import co.inajar.oursponsors.dbos.entities.User;
import co.inajar.oursponsors.models.user.UserResponse;
import co.inajar.oursponsors.services.user.UserManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserManager userManager;

    @InjectMocks
    private UserController userController;

    @Test
    void testGetUser() {

        // Mock the request headers
        Map<String, String> headers = Collections.singletonMap("GOOGLE-UID", "someGoogleUid");

        // Mock the expected user response
        User expectedUser = new User(); // Create a user instance with the desired data
        expectedUser.setName("John Smith");
        expectedUser.setEmail("john@gmail.com");
        expectedUser.setGoogleUid("someGoogleUid");
        UserResponse expectedResponse = new UserResponse(expectedUser);

        // Mock the userManager.getUserByGoogleUid() method to return the expected user
        when(userManager.getUserByGoogleUid(headers.get("GOOGLE-UID"))).thenReturn(Optional.of(expectedUser));

        // Invoke the getUser() method
        ResponseEntity<UserResponse> responseEntity = userController.getUser(headers);

        // Verify the response status code
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        // Verify the response body
        UserResponse responseBody = responseEntity.getBody();
        assertEquals(expectedResponse, responseBody);

    }

}
