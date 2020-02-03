package br.com.brainweb.interview.gateway.configuration;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.RoundRobinRule;
import org.springframework.cloud.consul.discovery.ConsulPing;
import org.springframework.context.annotation.Bean;

public class RibbonConfiguration {

    @Bean
    public IPing ribbonPing(final IClientConfig config) {
        return new ConsulPing();
    }

    @Bean
    public IRule ribbonRule(final IClientConfig config) {
        return new RoundRobinRule();
    }

}