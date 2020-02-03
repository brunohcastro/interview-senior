package br.com.brainweb.interview.gateway;

import br.com.brainweb.interview.gateway.configuration.RibbonConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
//@EnableDiscoveryClient
@SpringBootApplication
@RibbonClients(defaultConfiguration = RibbonConfiguration.class)
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }
}