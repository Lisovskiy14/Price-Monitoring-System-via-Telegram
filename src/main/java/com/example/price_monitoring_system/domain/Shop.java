package com.example.price_monitoring_system.domain;

import com.example.price_monitoring_system.utility.ToStringObjectParser;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Shop {
    private Long id;
    private String domain;
    private CssSelectorContainer cssSelectorContainer;

    @Override
    public String toString() {
        return ToStringObjectParser.parse(this);
    }
}
