package com.richardvinz.webflux_Playground.sec06.config;

import com.richardvinz.webflux_Playground.sec06.exceptions.CustomerNotFoundException;
import com.richardvinz.webflux_Playground.sec06.exceptions.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class RouterConfiguration {

    private final CustomerRequestHandler requestHandler;
    private final ApplicationExceptionHandler exceptionHandler;

    @Bean
    public RouterFunction<ServerResponse> customerRoutes() {
       return RouterFunctions.route()
               .GET("/customers", requestHandler::allCustomers)
               .GET("/customers/paginated", requestHandler::paginatedCustomer)
               .GET("/customers/{id}", requestHandler::getCustomer)
               .POST("/customers", requestHandler::saveCustomer)
               .PUT("/customers/{id}", requestHandler::updateCustomer)
               .DELETE("/customers/{id}", requestHandler::deleteCustomer)
               .onError(CustomerNotFoundException.class, this.exceptionHandler::handleException)
               .onError(InvalidInputException.class, this.exceptionHandler::handleException)
               .build();
    }
}
