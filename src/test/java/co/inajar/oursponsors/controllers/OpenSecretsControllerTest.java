package co.inajar.oursponsors.controllers;

import co.inajar.oursponsors.controllers.opensecrets.OpenSecretsController;
import co.inajar.oursponsors.dbos.entities.candidates.Contributor;
import co.inajar.oursponsors.dbos.entities.candidates.Sector;
import co.inajar.oursponsors.services.opensecrets.CandidatesApiManager;
import co.inajar.oursponsors.services.opensecrets.CandidatesManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OpenSecretsController.class)
public class OpenSecretsControllerTest {

    private static final String SECTOR_ID = "N00031820";

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @MockBean
    private CandidatesManager candidatesManager;

    @MockBean
    private CandidatesApiManager candidatesApiManager;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testGetSectors() throws Exception {

        ArrayList<Sector> sectors = new ArrayList<>();
        when(candidatesManager.getSectorsByCid(SECTOR_ID)).thenReturn(Optional.of(sectors));

        mockMvc.perform(post("/opensecrets/get_sectors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cid\": \"N00031820\"}"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    public void testGetContributors() throws Exception {

        ArrayList<Contributor> contributors = new ArrayList<>();
        when(candidatesManager.getContributorsByCid(SECTOR_ID)).thenReturn(Optional.of(contributors));

        mockMvc.perform(post("/opensecrets/get_contributors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cid\": \"N00031820\"}"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Configuration
    @EnableAutoConfiguration
    public static class TestConfig {
        @Bean
        public OpenSecretsController openSecretsController() {
            return new OpenSecretsController();
        }
    }
}
