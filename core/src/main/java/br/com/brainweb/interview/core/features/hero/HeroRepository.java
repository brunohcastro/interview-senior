package br.com.brainweb.interview.core.features.hero;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HeroRepository extends CrudRepository<HeroEntity, UUID> {

    @Query("SELECT * FROM hero WHERE lower(name) ILIKE :name")
    List<HeroEntity> findByName(String name);

    @Query("SELECT * FROM hero WHERE name = :name")
    Optional<HeroEntity> getByName(String name);

    @Modifying
    @Query("UPDATE hero SET enabled = true WHERE id = :id")
    void enable(UUID id);

    @Modifying
    @Query("UPDATE hero SET enabled = false WHERE id = :id")
    void disable(UUID id);

    @Query("SELECT EXISTS (SELECT 1 FROM hero WHERE name = :name)")
    boolean existsByName(String name);
}
