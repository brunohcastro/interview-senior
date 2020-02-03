package br.com.brainweb.interview.core.features.powerstats;

import br.com.brainweb.interview.model.PowerStats;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class PowerStatsService {

    private PowerStatsRepository repository;
    private ModelMapper mMapper;

    public PowerStatsService(PowerStatsRepository repository, ModelMapper mMapper) {
        this.repository = repository;
        this.mMapper = mMapper;
    }

    public Optional<PowerStats> findById(UUID id) {
        return this.repository.findById(id).map(it -> mMapper.map(it, PowerStats.class));
    }

    public void compare(PowerStats left, PowerStats right) {

    }
}
