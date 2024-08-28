package com.richardvinz.webflux_Playground;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;

@SpringBootApplication(scanBasePackages= "com.richardvinz.webflux_Playground.${sec}")
@EnableR2dbcRepositories(basePackages = "com.richardvinz.webflux_Playground.${sec}")
public class WebfluxPlaygroundApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebfluxPlaygroundApplication.class, args);
	}

}
