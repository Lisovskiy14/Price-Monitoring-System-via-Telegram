package com.example.price_monitoring_system.domain;

import lombok.Data;

@Data
public class Shop {
    private Long id;
    private String domain;
    private CssSelectorContainer cssSelectors;
}
