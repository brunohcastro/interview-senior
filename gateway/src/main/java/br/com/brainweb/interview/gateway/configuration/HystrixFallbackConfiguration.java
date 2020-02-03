package br.com.brainweb.interview.gateway.configuration;

import br.com.brainweb.interview.gateway.fallback.DefaultFallbackProvider;
import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HystrixFallbackConfiguration {

    @Bean
    public FallbackProvider zuulFallbackProvider() {
        return new DefaultFallbackProvider("iv-core");
    }
}