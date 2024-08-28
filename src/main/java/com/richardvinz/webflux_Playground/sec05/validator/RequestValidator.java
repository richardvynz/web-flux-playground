package com.richardvinz.webflux_Playground.sec05.validator;

import com.richardvinz.webflux_Playground.sec05.dto.CustomerDTO;
import com.richardvinz.webflux_Playground.sec05.exceptions.ApplicationExceptions;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class RequestValidator {

    public static UnaryOperator<Mono<CustomerDTO>> validate(){
        return mono -> mono.filter(hasName())
                .switchIfEmpty(ApplicationExceptions.missingName())
                .filter(hasValidEmail())
                .switchIfEmpty(ApplicationExceptions.missingValidEmail());
    }

    private static Predicate<CustomerDTO> hasName() {
        return dto->Objects.nonNull(dto.getName()) && !dto.getName().isBlank();
    }
    private static Predicate<CustomerDTO> hasValidEmail() {
        return dto -> Objects.nonNull(dto.getEmail()) && dto.getEmail().contains("@");
    }
}
