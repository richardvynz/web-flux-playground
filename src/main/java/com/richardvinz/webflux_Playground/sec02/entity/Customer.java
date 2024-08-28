package com.richardvinz.webflux_Playground.sec02.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Customer {
    @Id
    private int id;
    private String name;
    private String email;
}
