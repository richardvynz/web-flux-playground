package com.richardvinz.webflux_Playground.sec02;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {"sec=sec02",
        "logging.level.org.springframework.r2dbc=DEBUG"}
)
public abstract class AbstractTest {
}
