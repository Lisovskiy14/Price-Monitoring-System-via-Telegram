package com.example.price_monitoring_system.domain;

import com.example.price_monitoring_system.utility.ToStringObjectParser;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

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

    @Override
    public String toString() {
        return ToStringObjectParser.parse(this);
    }
}
