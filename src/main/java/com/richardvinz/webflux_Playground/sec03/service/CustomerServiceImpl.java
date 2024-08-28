package com.richardvinz.webflux_Playground.sec03.service;

import com.richardvinz.webflux_Playground.sec03.dto.CustomerDTO;
import com.richardvinz.webflux_Playground.sec03.entity.Customer;
import com.richardvinz.webflux_Playground.sec03.mapper.EntityDtoMapper;
import com.richardvinz.webflux_Playground.sec03.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Flux<CustomerDTO>getCustomerList(Integer page, Integer size){
        return this.customerRepository.findBy(PageRequest.of(page-1, size))
                .map(EntityDtoMapper::tocustomerDTO);
    }

    @Override
    public Flux<CustomerDTO> getAllCustomer() {
        return customerRepository.findAll().map(EntityDtoMapper::tocustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> getCustomerById(Integer id) {
        return customerRepository.findById(id).map(EntityDtoMapper::tocustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> saveCustomer(Mono<CustomerDTO> customer) {
        return customer.map(EntityDtoMapper::toCustomerEntity)
                .flatMap(customerRepository::save)
                .map(EntityDtoMapper::tocustomerDTO);
    }

    @Override
    public Mono<CustomerDTO> updateCustomer(Integer id, Mono<CustomerDTO> customer) {
       return customerRepository.findById(id)
                .flatMap(entity->customer)
                .map(EntityDtoMapper::toCustomerEntity)
                .doOnNext(c->c.setId(id))
                .flatMap(customerRepository::save)
                .map(EntityDtoMapper::tocustomerDTO);
    }

    @Override
    public Mono<Boolean> deleteCustomer(Integer id) {
        return this.customerRepository.deleteCustomerById(id);
    }

//    @Override
//    public Mono<Boolean> deleteCustomer(Integer id) {
//        return this.customerRepository.deleteById(id);
//    }


}
