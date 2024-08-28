package com.richardvinz.webflux_Playground.sec02.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Product {
    @Id
    private Integer id;
    private String description;
    private Integer price;
}
