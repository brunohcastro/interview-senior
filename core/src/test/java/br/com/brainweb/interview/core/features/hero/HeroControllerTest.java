package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.core.features.powerstats.PowerStatsService;
import br.com.brainweb.interview.model.Hero;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;

@WebAppConfiguration
@SpringBootTest(classes = {HeroController.class})
@RunWith(SpringRunner.class)
public class HeroControllerTest {

    @MockBean
    private HeroService heroService;

    @MockBean
    private PowerStatsService powerStatsService;

    @InjectMocks
    private HeroController heroController;

    private MockMvc mockMvc;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        this.heroController = new HeroController(heroService, powerStatsService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(heroController).build();
    }

    @Test
    public void find_shouldListAllWithoutFilters() throws Exception {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        List<Hero> heroes = Arrays.asList(new Hero().withId(id1), new Hero().withId(id2));

        when(heroService.find(null)).thenReturn(heroes);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/heroes"))
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(id1.toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(id2.toString()));
    }
}
