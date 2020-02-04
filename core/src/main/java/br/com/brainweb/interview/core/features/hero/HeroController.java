package br.com.brainweb.interview.core.features.hero;

import br.com.brainweb.interview.core.features.powerstats.PowerStatsDiff;
import br.com.brainweb.interview.core.features.powerstats.PowerStatsService;
import br.com.brainweb.interview.core.toggle.FeatureToggle;
import br.com.brainweb.interview.core.utils.PropertyUtils;
import br.com.brainweb.interview.model.Hero;
import br.com.brainweb.interview.model.PowerStats;
import org.springframework.beans.BeanUtils;
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
    @FeatureToggle(feature = "features.heroes.list")
    public List<Hero> find(@RequestParam(value = "name", required = false) String name) {
        return heroService.find(name);
    }

    @GetMapping("/{id}")
    @FeatureToggle(feature = "features.heroes.find")
    public Hero findById(@PathVariable("id") String id) {
        Hero hero = this.heroService.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        PowerStats powerStats = this.powerStatsService.findById(hero.getPowerStats().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        hero.setPowerStats(powerStats);

        return hero;
    }

    @GetMapping("/{id}/power-stats")
    @FeatureToggle(feature = "features.heroes.power-stats")
    public PowerStats getPowerStatsFor(@PathVariable("id") String id) {
        return this.heroService.findById(UUID.fromString(id))
                .flatMap(hero -> this.powerStatsService.findById(hero.getPowerStats().getId()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @FeatureToggle(feature = "features.heroes.create")
    public ResponseEntity<Void> create(@Valid @RequestBody final Hero body) {
        boolean exists = this.heroService.existsByName(body.getName());

        if (exists) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This hero is already registered");
        }

        Hero hero = this.heroService.create(body);

        return ResponseEntity.created(URI.create(hero.getId().toString())).build();
    }

    @PatchMapping("/{id}")
    @FeatureToggle(feature = "features.heroes.update")
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
    @FeatureToggle(feature = "features.heroes.enable")
    public ResponseEntity<Void> enable(@PathVariable("id") String id) {
        checkExistence(id);

        this.heroService.enable(UUID.fromString(id));

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/disable")
    @FeatureToggle(feature = "features.heroes.disable")
    public ResponseEntity<Void> disable(@PathVariable("id") String id) {
        checkExistence(id);

        this.heroService.disable(UUID.fromString(id));

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @FeatureToggle(feature = "features.heroes.delete")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        Hero hero = this.heroService.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        this.heroService.delete(hero);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/compare-stats")
    @FeatureToggle(feature = "features.heroes.compare")
    public PowerStatsDiff compare(@PathVariable("id") String referenceId, @RequestParam("other_id") String otherId) {
        Hero reference = this.heroService.findById(UUID.fromString(referenceId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        Hero other = this.heroService.findById(UUID.fromString(otherId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return this.powerStatsService.compare(reference.getPowerStats().getId(), other.getPowerStats().getId());
    }

    private void checkExistence(String id) {
        boolean exists = this.heroService.exists(UUID.fromString(id));

        if (!exists) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

}
