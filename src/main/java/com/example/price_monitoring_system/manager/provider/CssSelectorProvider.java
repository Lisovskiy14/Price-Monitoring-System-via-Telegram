package com.example.price_monitoring_system.manager.provider;

public interface CssSelectorProvider {
    boolean supports(String url);
    String provide(String url);
}
