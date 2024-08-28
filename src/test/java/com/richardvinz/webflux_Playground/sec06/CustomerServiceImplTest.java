package com.richardvinz.webflux_Playground.sec06;

import com.richardvinz.webflux_Playground.sec06.dto.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@SpringBootTest(properties = "sec06")
@AutoConfigureWebTestClient
class CustomerServiceImplTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImplTest.class);

    @Autowired
    private WebTestClient client;


    @Test
    public void getAllCustomers(){
        this.client.get().uri("/customers")
                .exchange().expectStatus().is2xxSuccessful()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(CustomerDTO.class)
                .value(list->log.info("{}",list))
                .hasSize(10);
    }

    @Test
    public void getPaginatedList(){
        this.client.get().uri("/customers/paginated?page=1&size=3")
                .exchange().expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(res->log.info("{}",new String(Objects.requireNonNull(res.getResponseBody()))))
                .jsonPath("$.length()").isEqualTo(3);
    }

    @Test
    public void getCustomerById(){
        this.client.get().uri("/customers/2")
                .exchange().expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(response-> log.info("{}",new String(Objects.requireNonNull(response.getResponseBody()))))
                .jsonPath("$.name").isEqualTo("mike")
                .jsonPath("$.email").isEqualTo("mike@gmail.com");
    }

    @Test
    public void createCustomer(){
        var dto = new CustomerDTO(0,"john","john@gmail.com");
        this.client.post().uri("/customers")
                .bodyValue(dto).exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(res->log.info("{}",new String(Objects.requireNonNull(res.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(12)
                .jsonPath("$.name").isEqualTo("john")
                .jsonPath("$.email").isEqualTo("john@gmail.com");
    }

    @Test
    public void updateCustomer(){
        var customerToUpdate = new CustomerDTO(6,"vincent","vincent@gmail.com");
        this.client.put().uri("/customers/"+customerToUpdate.getId())
                .bodyValue(customerToUpdate).exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(res->log.info("{}",new String(Objects.requireNonNull(res.getResponseBody()))))
                .jsonPath("$.name").isEqualTo("vincent")
                .jsonPath("$.email").isEqualTo("vincent@gmail.com");
    }
    @Test
    public void createAndDeleteCustomer(){
        var newCustomer = new CustomerDTO(0,"moses","moses@gmail.com");
        this.client.post().uri("/customers").bodyValue(newCustomer).exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(n->log.info("{}",new String(Objects.requireNonNull(n.getResponseBody()))))
                .jsonPath("$.email").isEqualTo("moses@gmail.com")
                .jsonPath("$.id").isEqualTo(11);

        this.client.delete().uri("/customers/11")
                .exchange().expectStatus().is2xxSuccessful()
                .expectBody().isEmpty();
    }

    @Test
    public void customerNotFound(){
        var absenteeCustomer = new CustomerDTO(80,"moses","moses@gmail.com");

        this.client.get().uri("/customers/"+absenteeCustomer.getId())
                .exchange().expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail")
                .isEqualTo("Customer [id = 80] is not found");


        this.client.delete().uri("/customers/"+absenteeCustomer.getId()).exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Customer [id = 80] is not found");


        this.client.put().uri("/customers/"+absenteeCustomer.getId())
                .bodyValue(absenteeCustomer).exchange().expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Customer [id = 80] is not found");
    }

    @Test
    public void invalidCustomerInput(){
        var invalidNameInput = new CustomerDTO(0,null,"michael@gmail.com");
        var invalidBlankNameInput = new CustomerDTO(0,"",null);
        var invalidEmailInput = new CustomerDTO(0,"michael",null);
        var invalidEmail = new CustomerDTO(0,"michael","michaelNational");

        this.client.post().uri("/customers")
                .bodyValue(invalidNameInput)
                .exchange().expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Name is required");

        this.client.post().uri("/customers").bodyValue(invalidBlankNameInput)
                .exchange().expectStatus().is4xxClientError().expectBody()
                .jsonPath("$.detail").isEqualTo("Name is required");


        this.client.post().uri("/customers").bodyValue(invalidEmailInput)
                .exchange().expectStatus().is4xxClientError()
                .expectBody().jsonPath("$.detail").isEqualTo("valid email is required");

        this.client.put().uri("/customers/10")
                .bodyValue(invalidEmail).exchange().expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("valid email is required");
                }
}