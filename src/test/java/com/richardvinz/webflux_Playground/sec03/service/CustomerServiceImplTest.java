package com.richardvinz.webflux_Playground.sec03.service;

import com.richardvinz.webflux_Playground.sec03.dto.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureWebTestClient
@SpringBootTest(properties ="sec03")
class CustomerServiceImplTest {

    private static final Logger log
            = LoggerFactory.getLogger(CustomerServiceImplTest.class);

    @Autowired
    private WebTestClient client;

    @Test
    public void getAllCustomers(){
        this.client.get()
                .uri("/customers/allCustomers")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CustomerDTO.class)
                .value(list-> log.info("{}", list))
                .hasSize(10);
    }

    @Test
    public void paginatedCustomer() {
        this.client.get().uri("/customers/paginated?page=3&size=2")
                .exchange().expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}",new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.length()").isEqualTo(2)
                .jsonPath("$.[0].name").isEqualTo("sophia")
                .jsonPath("$.[1].name").isEqualTo("liam");
    }
    @Test
    public void getCustomerById(){
        this.client.get().uri("/customers/1")
                .exchange().expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r->log.info("{}",new String(Objects.requireNonNull(r.getResponseBody()))))
//                .jsonPath("$.length()").isEqualTo(1)
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.name").isEqualTo("sam")
                .jsonPath("$.email").isEqualTo("sam@gmail.com");
    }

    @Test
    public void createAndDeleteCustomer(){
        var dto = new CustomerDTO(0,"ndubuisi","ndubuisi@gmail.com");

        this.client.post().uri("/customers").bodyValue(dto)
                .exchange().expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(response->log.info("{}",response.getResponseBody()))
                .jsonPath("$.id").isEqualTo(11)
                .jsonPath("$.name").isEqualTo("ndubuisi")
                .jsonPath("$.email").isEqualTo("ndubuisi@gmail.com");

        this.client.delete().uri("/customers/11")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody()
                .isEmpty();
    }

    @Test
    public void updateCustomer(){
        var dto = new CustomerDTO(0,"vincent","vincent@gmail.com");

        this.client.put().uri("/customers/10")
                .bodyValue(dto).exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(response->log.info("{}",
                        new String(Objects.requireNonNull(response.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(10)
                .jsonPath("$.name").isEqualTo("vincent")
                .jsonPath("$.email").isEqualTo("vincent@gmail.com");
    }

    @Test
    public void customerNotFound(){
        var absentCustomer = new CustomerDTO(700,"johnson","joachim");

        this.client.get().uri("/customers/700")
                .exchange().expectStatus()
                .is4xxClientError()
                .expectBody().isEmpty();

        this.client.put().uri("/customers/"+absentCustomer.getId())
                .bodyValue(absentCustomer)
                .exchange().expectStatus().is4xxClientError().expectBody().isEmpty();

        this.client.delete().uri("/customers/"+absentCustomer.getId())
                .exchange().expectStatus()
                .is4xxClientError()
                .expectBody().isEmpty();

    }
}