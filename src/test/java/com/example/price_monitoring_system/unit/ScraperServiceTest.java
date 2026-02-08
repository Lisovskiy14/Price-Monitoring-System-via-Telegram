package com.example.price_monitoring_system.unit;

import com.example.price_monitoring_system.service.ScraperService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ScraperServiceTest {

    private final String cssSelector = ".product-price__big.text-2xl.font-bold.leading-none";
    private final String url = "https://rozetka.com.ua/ua/378359865/p378359865/";

    @Autowired
    private ScraperService scraperService;

    @Test
    public void fetchPriceTest() {
        BigDecimal price = scraperService.fetchPrice(url, cssSelector);
        System.out.println(price);
        assertThat(price).isNotNull();
        assertThat(price).isOfAnyClassIn(BigDecimal.class);

    }
}
