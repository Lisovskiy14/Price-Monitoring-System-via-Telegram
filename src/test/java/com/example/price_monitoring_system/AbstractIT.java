package com.example.price_monitoring_system;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;

import java.util.TimeZone;

@SpringBootTest
@DirtiesContext
public abstract class AbstractIT {

    static final GenericContainer POSTGRES_CONTAINER = new GenericContainer("postgres:16")
            .withEnv("POSTGRES_USER", "postgres")
            .withEnv("POSTGRES_PASSWORD", "postgres")
            .withEnv("POSTGRES_DB", "price_monitoring_db")
            .withEnv("TZ", "UTC")
            .withExposedPorts(5432);

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        POSTGRES_CONTAINER.start();
    }

    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", () -> String.format("jdbc:postgresql://%s:%d/price_monitoring_db?options=-c%20timezone=UTC",
                POSTGRES_CONTAINER.getHost(), POSTGRES_CONTAINER.getMappedPort(5432)));
        registry.add("spring.datasource.username", () -> "postgres");
        registry.add("spring.datasource.password", () -> "postgres");
    }
}
