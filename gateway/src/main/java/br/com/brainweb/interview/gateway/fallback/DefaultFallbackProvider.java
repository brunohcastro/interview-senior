package br.com.brainweb.interview.gateway.fallback;

import org.springframework.cloud.netflix.zuul.filters.route.FallbackProvider;
import org.springframework.http.client.ClientHttpResponse;

public class DefaultFallbackProvider implements FallbackProvider {

    private String route;

    public DefaultFallbackProvider(String route) {
        this.route = route;
    }

    @Override
    public String getRoute() {
        return this.route;
    }

    @Override
    public ClientHttpResponse fallbackResponse(String route, Throwable cause) {
        return new ClientFallbackResponse();
    }
}
