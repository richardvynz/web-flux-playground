package com.richardvinz.webflux_Playground.sec06.config;

import com.richardvinz.webflux_Playground.sec06.dto.CustomerDTO;
import com.richardvinz.webflux_Playground.sec06.exceptions.ApplicationExceptions;
import com.richardvinz.webflux_Playground.sec06.service.CustomerService;
import com.richardvinz.webflux_Playground.sec06.validator.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerRequestHandler {
    private final CustomerService customerService;

    public Mono<ServerResponse>allCustomers(ServerRequest request){
        return this.customerService.getAllCustomer()
                .as(flux->ServerResponse.ok().body(flux, CustomerDTO.class));
    }


    public Mono<ServerResponse>paginatedCustomer(ServerRequest request){
        var page = request.queryParam("pageNumber").map(Integer::parseInt).orElse(1);
        var size = request.queryParam("size").map(Integer::parseInt).orElse(3);
        return this.customerService.getCustomerList(page, size)
                .collectList().flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse>getCustomer(ServerRequest request){
        var id = Integer.parseInt(request.pathVariable("id"));
        return this.customerService.getCustomerById(id)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                .flatMap(
                        ServerResponse.ok()::bodyValue
                );
//                .as(flux->ServerResponse.ok().body(flux,CustomerDTO.class));
    }
    public Mono<ServerResponse>saveCustomer(ServerRequest request){
        return request.bodyToMono(CustomerDTO.class)
                .transform(RequestValidator.validate())
                .as(this.customerService::saveCustomer)
                .flatMap(ServerResponse.ok()::bodyValue);
    }
    public Mono<ServerResponse>updateCustomer(ServerRequest request){
        var id = Integer.parseInt(request.pathVariable("id"));
        return request.bodyToMono(CustomerDTO.class)
                .transform(RequestValidator.validate())
                .as(validatedRequest->this.customerService.updateCustomer(id,validatedRequest))
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                .flatMap(ServerResponse.ok()::bodyValue);
    }
    public Mono<ServerResponse>deleteCustomer(ServerRequest request){
        var  id = Integer.parseInt(request.pathVariable("id"));
        return this.customerService.deleteCustomer(id)
                .filter(b->b)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
                .then(ServerResponse.ok().build());
    }
}
