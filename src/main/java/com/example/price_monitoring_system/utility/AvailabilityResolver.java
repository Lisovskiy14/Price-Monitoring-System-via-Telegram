package com.example.price_monitoring_system.utility;

public class AvailabilityResolver {

    public static boolean resolveByUrl(String url) {
        String availabilityType = url.substring(url.lastIndexOf("/") + 1)
                .replaceAll("[^a-zA-Z]", "")
                .toLowerCase();

        return availabilityType.equals("instock");
    }
}
