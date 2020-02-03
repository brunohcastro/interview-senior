package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.core.features.powerstats.PowerStatsEntity;
import br.com.brainweb.interview.core.features.powerstats.PowerStatsRepository;
import br.com.brainweb.interview.model.Hero;
import br.com.brainweb.interview.model.PowerStats;
import br.com.brainweb.interview.model.Race;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class HeroServiceTest {

    @MockBean
    private HeroRepository repository;

    @MockBean
    private PowerStatsRepository powerStatsRepository;

    @MockBean
    private ModelMapper mMapper;

    private HeroService heroService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        this.heroService = new HeroService(repository, powerStatsRepository, mMapper);
    }

    @Test
    public void find_shouldLoadAllHeroesWhenCalledWithoutFilter() {
        UUID uuid = UUID.randomUUID();
        List<HeroEntity> all = Collections.singletonList(new HeroEntity().withId(uuid));
        when(repository.findAll()).thenReturn(all);
        when(mMapper.map(any(), any())).thenReturn(new Hero().withId(uuid));

        List<Hero> heroes = this.heroService.find(null);

        Assertions.assertEquals(heroes.size(), all.size());
        Assertions.assertEquals(heroes.get(0).getId(), uuid);

        verify(repository, times(1)).findAll();
        verify(mMapper, times(1)).map(any(), any());
    }

    @Test
    public void find_shouldLoadHeroesFilteredByName() {
        UUID uuid = UUID.randomUUID();
        String name = "He-Man";

        HeroEntity heroEntity = new HeroEntity().withId(uuid);
        heroEntity.setName(name);

        Hero expected = new Hero().withId(uuid);
        expected.setName(name);

        List<HeroEntity> all = Arrays.asList(heroEntity, new HeroEntity().withId(UUID.randomUUID()));
        List<HeroEntity> filtered = Collections.singletonList(heroEntity);

        when(repository.findAll()).thenReturn(all);
        when(repository.findByName(eq("%" + name + "%"))).thenReturn(filtered);
        when(mMapper.map(eq(heroEntity), any())).thenReturn(expected);

        List<Hero> heroes = this.heroService.find(name);

        Assertions.assertNotEquals(heroes.size(), all.size());
        Assertions.assertEquals(heroes.size(), filtered.size());
        Assertions.assertEquals(heroes.get(0).getId(), expected.getId());
        Assertions.assertEquals(heroes.get(0).getName(), expected.getName());

        verify(repository, times(0)).findAll();
        verify(repository, times(1)).findByName(eq("%" + name + "%"));
        verify(mMapper, times(1)).map(any(), any());
    }

    @Test
    public void create_shouldCreateHero() {
        UUID heroId = UUID.randomUUID();
        UUID powerStatsId = UUID.randomUUID();

        PowerStats powerStats = new PowerStats();
        Hero hero = new Hero();

        hero.setPowerStats(powerStats);

        PowerStatsEntity powerStatsEntity = new PowerStatsEntity();
        HeroEntity heroEntity = new HeroEntity();

        final Hero expected = new Hero();
        expected.setPowerStats(new PowerStats());

        when(mMapper.map(eq(powerStats), any())).thenReturn(powerStatsEntity);
        when(mMapper.map(eq(hero), any())).thenReturn(heroEntity);
        when(powerStatsRepository.save(eq(powerStatsEntity))).thenReturn(powerStatsEntity.withId(powerStatsId));

        when(repository.save(eq(heroEntity))).then(it -> {
            HeroEntity outcome = it.getArgument(0);

            outcome.setId(heroId);

            return outcome;
        });

        when(mMapper.map(any(), eq(Hero.class))).then(it -> {
            HeroEntity outcome = it.getArgument(0);

            expected.setId(outcome.getId());
            expected.getPowerStats().setId(outcome.getPowerStatsId());

            return expected;
        });

        Hero result = this.heroService.create(hero);

        Assertions.assertNotNull(result.getId());
        Assertions.assertNotNull(result.getPowerStats().getId());
        Assertions.assertEquals(result.getPowerStats().getId(), powerStatsId);
        Assertions.assertEquals(result.getId(), heroId);

        verify(repository, times(1)).save(eq(heroEntity));
        verify(powerStatsRepository, times(1)).save(powerStatsEntity);
        verify(mMapper, times(3)).map(any(), any());
    }
}
