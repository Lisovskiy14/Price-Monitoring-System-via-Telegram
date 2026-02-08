package com.example.price_monitoring_system.manager.provider.impl;

import com.example.price_monitoring_system.manager.provider.CssSelectorProvider;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(10)
@Component
public class DatabaseCssSelectorProvider implements CssSelectorProvider {

    @Override
    public boolean supports(String url) {
        return false;
    }

    @Override
    public String provide(String url) {
        return "";
    }
}
