package com.richardvinz.webflux_Playground.sec06.service;

import com.richardvinz.webflux_Playground.sec06.dto.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
   Flux<CustomerDTO>getCustomerList(Integer page, Integer size);
   Flux<CustomerDTO>getAllCustomer();
   Mono<CustomerDTO>getCustomerById(Integer id);
   Mono<CustomerDTO>saveCustomer(Mono<CustomerDTO>customer);
   Mono<CustomerDTO>updateCustomer(Integer id, Mono<CustomerDTO> customer);
   Mono<Boolean>deleteCustomer(Integer id);
}
