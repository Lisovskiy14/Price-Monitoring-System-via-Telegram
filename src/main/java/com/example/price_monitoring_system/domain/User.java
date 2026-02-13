package com.example.price_monitoring_system.domain;

import com.example.price_monitoring_system.utility.ToStringObjectParser;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private Long id;

    @Override
    public String toString() {
        return ToStringObjectParser.parse(this);
    }
}
