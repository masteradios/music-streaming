package com.example.api_gateway.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.reactive.DeferringLoadBalancerExchangeFilterFunction;
import org.springframework.cloud.client.loadbalancer.reactive.LoadBalancedExchangeFilterFunction;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;

@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final WebClient webClient;

    public JwtValidationGatewayFilterFactory(WebClient.Builder webClientBuilder
            , @Value("${auth.service.url}") String authServiceUrl) {

        this.webClient = webClientBuilder
                        .baseUrl(authServiceUrl)
                        .build();

    }

    @Override
    public GatewayFilter apply(Object config) {

        return (exchange, chain) -> {

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            return webClient.get()
                    .uri("/auth/validate")
                    .header(HttpHeaders.AUTHORIZATION, authHeader)
                    .exchangeToMono(response -> {
                        if (response.statusCode().equals(HttpStatus.OK)) {
                            String userEmail = response.headers().header("X-USER-EMAIL").stream().findFirst().orElse(null);
                            if (userEmail != null) {

                                return chain.filter(exchange.mutate()
                                        .request(exchange.getRequest().mutate()
                                                .header("X-USER-EMAIL", userEmail)
                                                .build())
                                        .build());
                            }
                            return chain.filter(exchange); // fallback, no email
                        } else {
                            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            return exchange.getResponse().setComplete();
                        }
                    });
        };
    }





}
