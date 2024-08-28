package com.richardvinz.webflux_Playground.sec02;

import com.richardvinz.webflux_Playground.sec02.repoisitory.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.test.StepVerifier;

@SpringBootTest
public class Lec02ProductRepositoryTest extends AbstractTest{
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void findByPriceRange(){
        this.productRepository.findByPriceBetween(750,1000)
                .doOnNext(c->log.info("{}",c))
                .as(StepVerifier::create)
                .expectNextCount(3)
                .verifyComplete();
    }
    @Test
    public void pageable(){
      this.productRepository
              .findBy(PageRequest.of(0,3)
                      .withSort(Sort.by("price").ascending()))
              .doOnNext(p->log.info("{}",p))
              .as(StepVerifier::create)
              .assertNext(c-> Assertions.assertEquals(200,c.getPrice()))
              .assertNext(c-> Assertions.assertEquals(250,c.getPrice()))
              .assertNext(c-> Assertions.assertEquals(300,c.getPrice()))
              .verifyComplete();
    }
}
