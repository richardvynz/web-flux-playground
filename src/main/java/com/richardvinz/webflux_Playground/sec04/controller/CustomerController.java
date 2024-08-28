package com.richardvinz.webflux_Playground.sec04.controller;

import com.richardvinz.webflux_Playground.sec04.dto.CustomerDTO;
import com.richardvinz.webflux_Playground.sec04.exceptions.ApplicationExceptions;
import com.richardvinz.webflux_Playground.sec04.service.CustomerService;
import com.richardvinz.webflux_Playground.sec04.validator.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;


    @GetMapping("/paginated")
    public Mono<List<CustomerDTO>> allCustomers(@RequestParam (defaultValue = "1") Integer pageNumber,
                                                @RequestParam (defaultValue = "3") Integer size){
        return service.getCustomerList(pageNumber, size).collectList();
    }

    @GetMapping("/allCustomers")
    public Flux<CustomerDTO>getCustomerList(){
        return service.getAllCustomer();
    }


    @GetMapping("/{id}")
    public Mono<CustomerDTO>getCustomer(@PathVariable Integer id){
        return service.getCustomerById(id)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id));
    }
    @PostMapping
    public Mono<CustomerDTO>createCustomer(@RequestBody Mono<CustomerDTO> customer){
        return customer.transform(RequestValidator.validate())
                .as(this.service::saveCustomer);
    }


    @PutMapping("/{id}")
    public Mono<CustomerDTO>updateCustomer(@PathVariable Integer id,
                                           @RequestBody Mono<CustomerDTO> customer){
        return customer.transform(RequestValidator.validate())
                .as(validRequest -> this.service.updateCustomer(id,validRequest))
                .switchIfEmpty(ApplicationExceptions.customerNotFound(id));
    }

    @DeleteMapping("/{id}")
    public Mono<Void>deleteCustomer(@PathVariable Integer id){
       return this.service.deleteCustomer(id)
               .filter(deleted->deleted)
               .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
               .then();
    }
}
