package com.richardvinz.webflux_Playground.sec02;

import com.richardvinz.webflux_Playground.sec02.entity.Customer;
import com.richardvinz.webflux_Playground.sec02.repoisitory.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

@SpringBootTest
public class Lec01CustomerRepositoryTest extends AbstractTest {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void findAll() {
        this.customerRepository.findAll()
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .expectNextCount(10)
                .expectComplete()
                .verify();
    }

    @Test
    public void findById() {
        this.customerRepository.findById(2)
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("mike", c.getName()))
//                .verifyComplete();
                .expectComplete()
                .verify();
    }

    @Test
    public void findByName() {
        this.customerRepository.findByName("jake")
                .doOnNext(c -> log.info("{}", c.getName()))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("jake@gmail.com", c.getEmail()))
                .verifyComplete();
    }

    @Test
    public void findByEmailEndingWith() {
        this.customerRepository.findByEmailEndingWith("ke@gmail.com")
                .doOnNext(c -> log.info("{}", c.getEmail()))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("mike@gmail.com", c.getEmail()))
                .assertNext(c -> Assertions.assertEquals("jake@gmail.com", c.getEmail()))
                .verifyComplete();
    }

    @Test
    public void insertAndDeleteCustomer() {
        var customer = new Customer();
        customer.setName("ndubuisi");
        customer.setEmail("ndubuisi@gmail.com");
        this.customerRepository.save(customer)
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertNotNull(c.getId()))
                .verifyComplete();

//        count
        this.customerRepository.count()
                .as(StepVerifier::create)
                .expectNext(11l)
                .verifyComplete();

//        create

        this.customerRepository.deleteById(11)
                .then(this.customerRepository.count())
                .as(StepVerifier::create)
                .expectNext(10l)
                .verifyComplete();
    }

    @Test
    public void updateCustomerName() {
        this.customerRepository.findByName("ethan")
                .doOnNext(c -> c.setName("emmanuel"))
//                .flatMap(c->customerRepository.save(c))
                .flatMap(customerRepository::save)
                .doOnNext(c -> log.info("{}", c))
                .as(StepVerifier::create)
                .assertNext(c -> Assertions.assertEquals("emmanuel", c.getName()))
                .verifyComplete();
    }

}
