package com.example.price_monitoring_system.util;

import com.example.price_monitoring_system.common.Availability;

import java.util.Set;

public class AvailabilityResolver {
    private static final Set<Availability> AVAILABLE_SET = Set.of(
            Availability.IN_STOCK, Availability.PRE_ORDER, Availability.BACK_ORDER, Availability.ONLINE_ONLY,
            Availability.IN_STORE_ONLY, Availability.LIMITED_AVAILABILITY, Availability.PRE_SALE
    );

    public static Availability resolveByUrl(String url) {
        String rawType = url.substring(url.lastIndexOf("/") + 1)
                .replaceAll("[^a-zA-Z]", "");

        StringBuilder sb = new StringBuilder();
        for (char c : rawType.toCharArray()) {
            if (Character.isUpperCase(c) && !sb.isEmpty()) {
                sb.append('-');
            }
            sb.append(Character.toLowerCase(c));
        }
        String processedType = sb.toString();

        for (Availability availability : Availability.values()) {
            if (availability.getValue().equals(processedType)) {
                return availability;
            }
        }

        return Availability.OUT_OF_STOCK;
    }

    public static boolean becomeAvailable(Availability newAvailability, Availability prevAvailability) {
        if (AVAILABLE_SET.contains(prevAvailability)) {
            return false;
        }
        return AVAILABLE_SET.contains(newAvailability);
    }
}
