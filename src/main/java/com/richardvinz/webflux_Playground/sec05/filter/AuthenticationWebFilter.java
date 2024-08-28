package com.richardvinz.webflux_Playground.sec05.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;

import static com.richardvinz.webflux_Playground.sec05.filter.Category.PRIME;
import static com.richardvinz.webflux_Playground.sec05.filter.Category.STANDARD;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
@Order(1)
public class AuthenticationWebFilter implements WebFilter {

    @Autowired
    private FilterErrorHandler errorHandler;

    private static final Map<String,Category>TOKEN_CATEGORY_MAP = Map.of(
            "secret123",STANDARD,
            "secret456",PRIME
    );
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        String token = exchange.getRequest().getHeaders().getFirst("auth-token");

        if(Objects.nonNull(token) && TOKEN_CATEGORY_MAP.containsKey(token)){
            exchange.getAttributes().put("category",TOKEN_CATEGORY_MAP.get(token));
            return chain.filter(exchange);
        }
        return errorHandler.sendProblemDetails(exchange, UNAUTHORIZED,"invalid Authorization token provided");
//        return Mono.fromRunnable(()->exchange.getResponse().setStatusCode(UNAUTHORIZED));
    }
}
