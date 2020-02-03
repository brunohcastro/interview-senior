package br.com.brainweb.interview.core.configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfiguration {

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    public Config hazelcastConfig() {
        MapConfig mapConfig = new MapConfig()
                .setName("heroes")
                .setMaxSizeConfig(new MaxSizeConfig(200, MaxSizeConfig.MaxSizePolicy.FREE_HEAP_SIZE))
                .setEvictionPolicy(EvictionPolicy.LRU)
                .setTimeToLiveSeconds(20);

        Config config = new Config()
                .setInstanceName(appName)
                .addMapConfig(mapConfig);

        return config;
    }
}
