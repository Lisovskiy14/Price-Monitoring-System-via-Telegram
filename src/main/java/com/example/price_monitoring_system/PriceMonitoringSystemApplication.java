package com.example.price_monitoring_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PriceMonitoringSystemApplication {

    private final String cssSelector = "product-price__big text-2xl font-bold leading-none";

	public static void main(String[] args) {
		SpringApplication.run(PriceMonitoringSystemApplication.class, args);
	}

}
