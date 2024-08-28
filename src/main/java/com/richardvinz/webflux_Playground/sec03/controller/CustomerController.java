package com.richardvinz.webflux_Playground.sec03.controller;

import com.richardvinz.webflux_Playground.sec02.repoisitory.CustomerRepository;
import com.richardvinz.webflux_Playground.sec03.dto.CustomerDTO;
import com.richardvinz.webflux_Playground.sec03.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService service;


    @GetMapping("/paginated")
    public Mono<List<CustomerDTO>> allCustomers(@RequestParam (defaultValue = "1") Integer page,
                                          @RequestParam (defaultValue = "3") Integer size){

        Mono<List<CustomerDTO>> listMono = service.getCustomerList(page, size).collectList();
        return listMono;
    }

    @GetMapping("/allCustomers")
    public Flux<CustomerDTO>getCustomerList(){
        return service.getAllCustomer();
    }
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerDTO>>getCustomer(@PathVariable Integer id){
        return service.getCustomerById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @PostMapping
    public Mono<CustomerDTO>createCustomer(@RequestBody Mono<CustomerDTO> customer){
        return service.saveCustomer(customer);
    }
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CustomerDTO>>updateCustomer(@PathVariable Integer id, @RequestBody Mono<CustomerDTO> customer){
        return service.updateCustomer(id, customer)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>>deleteCustomer(@PathVariable Integer id){
       return this.service.deleteCustomer(id)
               .filter(deleted->deleted)
               .map(deleted->ResponseEntity.ok().<Void>build())
               .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
