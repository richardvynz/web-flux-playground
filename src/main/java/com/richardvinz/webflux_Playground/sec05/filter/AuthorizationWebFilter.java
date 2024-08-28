package com.richardvinz.webflux_Playground.sec05.filter;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import static com.richardvinz.webflux_Playground.sec05.filter.Category.STANDARD;

@Service
@Order(2)
public class AuthorizationWebFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        Category category = exchange.getAttributeOrDefault("category", STANDARD);
        return switch (category) {
            case STANDARD->standard(exchange,chain);
            case PRIME -> prime(exchange,chain);
        };
    }

    private Mono<Void>prime(ServerWebExchange exchange,WebFilterChain chain){
        return chain.filter(exchange);
    }

    private Mono<Void>standard(ServerWebExchange exchange, WebFilterChain chain){
        var isGet = HttpMethod.GET.equals(exchange.getRequest().getMethod());
        if(isGet){
            return chain.filter(exchange);
        }
        return Mono.fromRunnable(()->exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN));
    }
}
