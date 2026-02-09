package com.example.price_monitoring_system.utility;

import com.example.price_monitoring_system.service.exception.ScraperConnectionFailedException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HtmlDocumentProvider {

    @Value("${scraping.user-agent}")
    private static String USER_AGENT;

    @Value("${scraping.timeout}")
    private static int TIMEOUT;

    public static Document provideHtmlDoc(String url) {
        try {
            return Jsoup.connect(url)
                    .userAgent(USER_AGENT)
                    .timeout(TIMEOUT)
                    .get();

        } catch (IOException ex) {
            throw new ScraperConnectionFailedException(ex.getMessage());
        }
    }
}
