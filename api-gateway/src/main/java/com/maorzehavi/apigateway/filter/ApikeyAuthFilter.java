package com.maorzehavi.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ApikeyAuthFilter extends AbstractGatewayFilterFactory<ApikeyAuthFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    public ApikeyAuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (!exchange.getRequest().getHeaders().containsKey("x-api-key")) {
                return chain.filter(exchange);
            }

            String authHeader = exchange.getRequest().getHeaders().get("x-api-key").get(0);

            return webClientBuilder.build()
                    .post()
                    .uri("http://identity-service/api/v1/auth/validate/apikey")
                    .body(Mono.just(authHeader), String.class)
                    .retrieve().bodyToMono(Boolean.class)
                    .map(isValid -> {
                        if (!isValid) {
                            throw new RuntimeException("Invalid api key");
                        }
                        return exchange;
                    }).flatMap(chain::filter);
        };
    }

    public static class Config {
        // empty class as I don't need any particular configuration
    }
}