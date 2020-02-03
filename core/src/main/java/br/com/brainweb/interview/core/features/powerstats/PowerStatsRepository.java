package br.com.brainweb.interview.core.features.powerstats;

import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface PowerStatsRepository extends CrudRepository<PowerStatsEntity, UUID> {
}
