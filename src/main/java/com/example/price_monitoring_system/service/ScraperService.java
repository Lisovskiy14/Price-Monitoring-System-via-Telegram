package com.example.price_monitoring_system.service;

import com.example.price_monitoring_system.service.exception.ElementNotFoundException;
import com.example.price_monitoring_system.service.exception.ScraperConnectionFailedException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public class ScraperService {

    @Value("${scraping.user-agent}")
    private String USER_AGENT;

    @Value("${scraping.timeout}")
    private int TIMEOUT;

    public BigDecimal fetchPrice(String url, String cssSelector) {
        try {

            Document doc = Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .timeout(TIMEOUT)
                    .get();

            Element priceElement = doc.selectFirst(cssSelector);

            if (priceElement == null) {
                throw new ElementNotFoundException(cssSelector);
            }

            String rowPrice = priceElement.text()
                    .replaceAll("[^0-9,.]", "")
                    .replace(",", ".");

            return new BigDecimal(rowPrice);

        } catch (IOException ex) {
            throw new ScraperConnectionFailedException(ex.getMessage());
        }
    }
}
