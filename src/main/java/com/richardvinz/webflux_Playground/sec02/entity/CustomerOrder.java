package com.richardvinz.webflux_Playground.sec02.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Data
public class CustomerOrder {
    @Id
    private UUID orderId;
    private Integer customerId;
    private Integer productId;
    private Integer amount;
    private Instant orderDate;
}
