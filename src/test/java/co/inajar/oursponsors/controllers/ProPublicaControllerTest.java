package co.inajar.oursponsors.controllers;

import co.inajar.oursponsors.controllers.propublica.ProPublicaController;
import co.inajar.oursponsors.dbos.entities.User;
import co.inajar.oursponsors.dbos.entities.chambers.Congress;
import co.inajar.oursponsors.dbos.entities.chambers.Senator;
import co.inajar.oursponsors.services.propublica.MembersManager;
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

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ProPublicaController.class)
public class ProPublicaControllerTest {

    private static final String GOOGLE_UID = "google-uid";

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @MockBean
    private MembersManager membersManager;

    @MockBean
    private UserManager userManager;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testGetSenators() throws Exception {

        User user = new User();
        when(userManager.getUserByGoogleUid(GOOGLE_UID)).thenReturn(Optional.of(user));

        ArrayList<Senator> senators = new ArrayList<>();
        when(membersManager.getSenators(user)).thenReturn(Optional.of(senators));
        mockMvc.perform(get("/propublica/get_senators"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testGetCongress() throws Exception {

        User user = new User();
        when(userManager.getUserByGoogleUid(GOOGLE_UID)).thenReturn(Optional.of(user));

        ArrayList<Congress> congress = new ArrayList<>();
        when(membersManager.getCongress(user)).thenReturn(Optional.of(congress));
        mockMvc.perform(get("/propublica/get_congress"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Configuration
    @EnableAutoConfiguration
    public static class TestConfig {
        @Bean
        public ProPublicaController proPublicaController() {
            return new ProPublicaController();
        }
    }
}
