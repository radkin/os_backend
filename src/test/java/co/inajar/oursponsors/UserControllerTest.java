import co.inajar.oursponsors.controllers.user.UserController;
import co.inajar.oursponsors.dbos.entities.User;
import co.inajar.oursponsors.services.user.UserManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    private static final String GOOGLE_UID = "google-uid";

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @MockBean
    private UserManager userManager;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testGetUsers() throws Exception {

        User user = new User();
        when(userManager.getUserByGoogleUid(GOOGLE_UID)).thenReturn(Optional.of(user));
        mockMvc.perform(get("/user/get_user"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Configuration
    @EnableAutoConfiguration
    public static class TestConfig {
        @Bean
        public UserController userController() {
            return new UserController();
        }
    }

}