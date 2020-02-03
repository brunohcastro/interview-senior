package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.core.SpringIntegrationTest;
import br.com.brainweb.interview.model.Hero;
import br.com.brainweb.interview.model.PowerStats;
import br.com.brainweb.interview.model.Race;
import com.fasterxml.jackson.databind.ObjectMapper;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ActiveProfiles("it")
public class HeroControllerIT extends SpringIntegrationTest {

    @Autowired
    HeroController heroController;
    ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    private ResultActions resultActions;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(heroController).build();
    }

    @When("^the client calls /api/v1/heroes$")
    public void the_client_makes_POST_heroes() throws Throwable {
        Hero hero = new Hero();
        PowerStats powerStats = new PowerStats();

        powerStats.setStrength(10);
        powerStats.setIntelligence(10);
        powerStats.setDexterity(10);
        powerStats.setAgility(10);

        hero.setName("She-ra");
        hero.setRace(Race.HUMAN);
        hero.setPowerStats(powerStats);

        this.resultActions = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/v1/heroes").contentType("application/json").content(objectMapper.writeValueAsString(hero))
        );
    }

    @Then("^the client receives status code of (\\d+)$")
    public void the_client_receives_status_code_of(int statusCode) throws Throwable {
        resultActions.andExpect(MockMvcResultMatchers.status().is(statusCode));
    }

    @And("^the client receives a Location Header$")
    public void the_client_receives_a_Location_Header() throws Throwable {
        resultActions.andExpect(MockMvcResultMatchers.header().exists("Location"));
    }


}
