package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.core.features.powerstats.PowerStatsService;
import br.com.brainweb.interview.core.toggle.FeatureToggle;
import br.com.brainweb.interview.core.utils.PropertyUtils;
import br.com.brainweb.interview.model.Hero;
import br.com.brainweb.interview.model.PowerStats;
import org.springframework.beans.BeanUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/heroes")
public class HeroController {

    private final HeroService heroService;
    private final PowerStatsService powerStatsService;

    private Environment env;

    public HeroController(HeroService heroService, PowerStatsService powerStatsService, Environment env) {
        this.heroService = heroService;
        this.powerStatsService = powerStatsService;
        this.env = env;
    }

    @GetMapping
    @FeatureToggle(feature = "feature.hero.enabled")
    public List<Hero> find(@RequestParam(value = "name", required = false) String name) {
        return heroService.find(name);
    }

    @GetMapping("/{id}")
    public Hero findById(@PathVariable("id") String id) {
        Hero hero = this.heroService.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        PowerStats powerStats = this.powerStatsService.findById(hero.getPowerStats().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        hero.setPowerStats(powerStats);

        return hero;
    }

    @GetMapping("/{id}/power-stats")
    public PowerStats getPowerStatsFor(@PathVariable("id") String id) {
        return this.heroService.findById(UUID.fromString(id))
                .flatMap(hero -> this.powerStatsService.findById(hero.getPowerStats().getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody final Hero body) {
        boolean exists = this.heroService.existsByName(body.getName());

        if (exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This hero is already registered");
        }

        Hero hero = this.heroService.create(body);

        return ResponseEntity.created(URI.create(hero.getId().toString())).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") final String id, @RequestBody final Hero body) {
        final UUID uuid = UUID.fromString(id);

        final Optional<Hero> existingHero = this.heroService.getByName(body.getName())
                .filter(it -> !it.getId().equals(uuid));

        if (existingHero.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There's already a hero with this name");
        }

        final Hero hero = this.heroService.findById(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        final PowerStats powerStats = this.powerStatsService.findById(hero.getPowerStats().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        BeanUtils.copyProperties(body, hero, PropertyUtils.getNullPropertyNames(body));
        BeanUtils.copyProperties(body.getPowerStats(), powerStats, PropertyUtils.getNullPropertyNames(body.getPowerStats()));

        hero.setPowerStats(powerStats);

        this.heroService.update(hero);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/enable")
    public ResponseEntity<Void> enable(@PathVariable("id") String id) {
        checkExistence(id);

        this.heroService.enable(UUID.fromString(id));

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/disable")
    public ResponseEntity<Void> disable(@PathVariable("id") String id) {
        checkExistence(id);

        this.heroService.disable(UUID.fromString(id));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        Hero hero = this.heroService.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        this.heroService.delete(hero);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/compare")
    public ResponseEntity<Void> compare(@RequestParam("first") String firstId, @RequestParam("second") String secondId) {
        return null;
    }

    private void checkExistence(String id) {
        boolean exists = this.heroService.exists(UUID.fromString(id));

        if (!exists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
