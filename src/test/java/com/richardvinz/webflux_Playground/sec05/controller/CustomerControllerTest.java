package com.richardvinz.webflux_Playground.sec05.controller;

import com.richardvinz.webflux_Playground.sec03.dto.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@SpringBootTest(properties = "sec05")
@AutoConfigureWebTestClient
class CustomerControllerTest {

    @Autowired
    private WebTestClient client;


    @Test
    public void authorizeRequest(){
        this.client.get()
                .uri("/customers")
                .exchange()
                .expectStatus().isEqualTo(UNAUTHORIZED);

        this.validateGet("secret",UNAUTHORIZED);
        this.validatePost("secret456",OK);
    }

    private void validateGet(String token, HttpStatus expectedStatus){
        this.client.get()
                .uri("/customers")
                .header("auth-token")
                .exchange()
                .expectStatus().isEqualTo(expectedStatus);
    }

    private void validatePost(String token, HttpStatus expectedStatus){
        var dto = new CustomerDTO(0,"marshal","marshal@gmail.com");
        this.client.post().uri("/customers")
                .bodyValue(dto)
                .header("auth-token",token)
                .exchange().expectStatus().isEqualTo(expectedStatus);
    }
}