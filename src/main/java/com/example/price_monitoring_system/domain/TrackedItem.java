package com.example.price_monitoring_system.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TrackedItem {
    private Long id;
    private Product product;
    private Shop shop;
    private String url;
    private LocalDateTime lastChecked;
    private List<User> listeners;

    public void addListener(User user) {
        listeners.add(user);
    }

    public void removeListener(User user) {
        listeners.remove(user);
    }

    @Override
    public String toString() {
        return String.format("[%s : %s]", shop.getDomain(), product.getName());
    }
}
