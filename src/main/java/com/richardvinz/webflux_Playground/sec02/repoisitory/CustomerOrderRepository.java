package com.richardvinz.webflux_Playground.sec02.repoisitory;

import com.richardvinz.webflux_Playground.sec02.dto.OrderDetails;
import com.richardvinz.webflux_Playground.sec02.entity.CustomerOrder;
import com.richardvinz.webflux_Playground.sec02.entity.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface CustomerOrderRepository
        extends ReactiveCrudRepository<CustomerOrder, UUID> {

    @Query("""
            SELECT
                p.*
            FROM
                customer c
            INNER JOIN customer_order co ON c.id = co.customer_id
            INNER JOIN product p ON co.product_id = p.id
            WHERE
                c.name = :name
            """)
    Flux<Product>getProductsOrderedByCustomer(String name);

    @Query("""
            SELECT
                co.order_id,
                c.name AS customer_name,
                p.description AS product_name,
                co.amount,
                co.order_date
            FROM
                customer c
            INNER JOIN customer_order co ON c.id = co.customer_id
            INNER JOIN product p ON p.id = co.product_id
            WHERE
                p.description = :description
            ORDER BY co.amount DESC
            """)
    Flux<OrderDetails>getOrderDetailsByProducts(String description);
}
