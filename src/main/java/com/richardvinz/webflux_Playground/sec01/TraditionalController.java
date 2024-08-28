package com.richardvinz.webflux_Playground.sec01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/traditional")
public class TraditionalController {
    private static final Logger log = LoggerFactory.getLogger(TraditionalController.class);
    private RestClient restClient = RestClient.builder().baseUrl("http://localhost:7070").build();

    @GetMapping("/products")
    public List<Product> getProducts() {
        var list = this.restClient.get().uri("/demo01/products/notorious").retrieve().body(new ParameterizedTypeReference<List<Product>>() {
        });

        log.info("received response: {}", list);
        return list;
    }
}
