package com.maorzehavi.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    public JwtAuthFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            if (exchange.getRequest().getHeaders().containsKey("x-api-key")){
                return chain.filter(exchange);
            }
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                throw new RuntimeException("Missing authorization information");
            }

            String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

            String[] parts = authHeader.split(" ");

            if (parts.length != 2 || !"Bearer".equals(parts[0])) {
                throw new RuntimeException("Incorrect authorization structure");
            }

//            return webClientBuilder.build()
//                    .post()
//                    .uri("http://identity-service/api/v1/auth/validate")
//                    .body(Mono.just(parts[1]), String.class)
//                    .retrieve().bodyToMono(Long.class)
//                    .map(userId -> {
//                        exchange.getRequest()
//                                .mutate()
//                                .header("X-auth-user-id", userId.toString())
//                                .build();
//                        return exchange;
//                    }).flatMap(chain::filter);
            return webClientBuilder.build()
                    .post()
                    .uri("http://identity-service/api/v1/auth/validate")
                    .body(Mono.just(parts[1]), String.class)
                    .retrieve().bodyToMono(Long.class)
                    .map(userId -> {
                        exchange.getRequest()
                                .mutate()
                                .header("X-auth-user-id", userId.toString())
                                .build();
                        return exchange;
                    }).flatMap(chain::filter);
        };
    }

    public static class Config {
        // empty class as I don't need any particular configuration
    }
}