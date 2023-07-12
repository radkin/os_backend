package co.inajar.oursponsors.controllers;

import co.inajar.oursponsors.controllers.opensecrets.OpenSecretsController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(OpenSecretsController.class)
public class OpenSecretsControllerTest {

    @Autowired
    private WebApplicationContext wac;
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testGetSectors() throws Exception {
        mockMvc.perform(get("/opensecrets/get_sectors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cid\": \"N00031820\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].sector_name").value("Finance/Insur/RealEst"))
                .andExpect(jsonPath("$[0].total").value("1657201"));
    }

    @Configuration
    @EnableAutoConfiguration
    public static class Config {
        @Bean
        public OpenSecretsController openSecretsController() {
            return new OpenSecretsController();
        }
    }
}
