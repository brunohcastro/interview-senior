package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.core.features.powerstats.PowerStatsEntity;
import br.com.brainweb.interview.core.features.powerstats.PowerStatsRepository;
import br.com.brainweb.interview.model.Hero;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HeroService {

    private final HeroRepository repository;
    private final PowerStatsRepository powerStatsRepository;
    private final ModelMapper mMapper;

    public HeroService(HeroRepository repository, PowerStatsRepository powerStatsRepository, ModelMapper mMapper) {
        this.repository = repository;
        this.powerStatsRepository = powerStatsRepository;
        this.mMapper = mMapper;
    }

    public List<Hero> find(String name) {
        List<HeroEntity> heroEntities = new ArrayList<>();
        if (name != null) {
            heroEntities = this.repository.findByName("%" + name + "%");
        } else {
            this.repository.findAll().forEach(heroEntities::add);
        }

        return heroEntities.stream().map(it -> mMapper.map(it, Hero.class)).collect(Collectors.toList());
    }

    public Optional<Hero> findById(UUID id) {
        return this.repository.findById(id).map(it -> mMapper.map(it, Hero.class));
    }

    public Optional<Hero> getByName(String name) {
        return this.repository.getByName(name).map(it -> mMapper.map(it, Hero.class));
    }

    @Transactional
    public Hero create(Hero hero) {
        PowerStatsEntity powerStatsEntity = mMapper.map(hero.getPowerStats(), PowerStatsEntity.class);
        HeroEntity heroEntity = mMapper.map(hero, HeroEntity.class);

        powerStatsEntity = powerStatsRepository.save(powerStatsEntity);

        heroEntity.setPowerStatsId(powerStatsEntity.getId());

        heroEntity = repository.save(heroEntity);

        return mMapper.map(heroEntity, Hero.class);
    }

    @Transactional
    public void update(Hero hero) {
        HeroEntity heroEntity = mMapper.map(hero, HeroEntity.class);
        PowerStatsEntity powerStatsEntity = mMapper.map(hero.getPowerStats(), PowerStatsEntity.class);

        repository.save(heroEntity);
        powerStatsRepository.save(powerStatsEntity);
    }

    @Transactional
    public void enable(UUID id) {
        repository.enable(id);
    }

    @Transactional
    public void disable(UUID id) {
        repository.disable(id);
    }

    @Transactional
    public void delete(Hero hero) {
        repository.deleteById(hero.getId());
        powerStatsRepository.deleteById(hero.getPowerStats().getId());
    }

    public boolean exists(UUID id) {
        return this.repository.existsById(id);
    }

    public boolean existsByName(String name) {
        return this.repository.existsByName(name);
    }
}
