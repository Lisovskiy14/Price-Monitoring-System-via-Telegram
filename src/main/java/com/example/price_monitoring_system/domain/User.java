package com.example.price_monitoring_system.domain;

import com.example.price_monitoring_system.utility.ToStringObjectParser;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Objects;

@Data
@Builder
public class User {

    private Long id;

    @Override
    public String toString() {
        return ToStringObjectParser.parse(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof User user)) return false;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
