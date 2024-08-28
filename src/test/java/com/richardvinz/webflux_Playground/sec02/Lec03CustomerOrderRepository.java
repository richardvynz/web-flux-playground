package com.richardvinz.webflux_Playground.sec02;

import com.richardvinz.webflux_Playground.sec02.repoisitory.CustomerOrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

public class Lec03CustomerOrderRepository extends AbstractTest{

    private static final Logger log = LoggerFactory.getLogger(Lec03CustomerOrderRepository.class);

    @Autowired
    private CustomerOrderRepository orderRepository;

    @Test
    public void productOrderedByCustomer(){
        this.orderRepository.getProductsOrderedByCustomer("mike")
                .doOnNext(c->log.info("{}",c))
                .as(StepVerifier::create)
                .expectNextCount(2)
                .verifyComplete();
    }
    @Test
    public void OrderDetailByProducts(){
        this.orderRepository.getOrderDetailsByProducts("iphone 20")
                .doOnNext(dto->log.info("{}",dto))
                .as(StepVerifier::create)
                .assertNext(dto-> Assertions.assertEquals(975,dto.amount()))
                .assertNext(dto-> Assertions.assertEquals(950,dto.amount()))
                .verifyComplete();
    }
}
