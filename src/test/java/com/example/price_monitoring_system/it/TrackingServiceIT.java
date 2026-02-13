package com.example.price_monitoring_system.it;

import com.example.price_monitoring_system.AbstractIT;
import com.example.price_monitoring_system.domain.TrackedItem;
import com.example.price_monitoring_system.dto.TrackedItemRequestDto;
import com.example.price_monitoring_system.service.TrackingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("TrackingService IT")
@Tag("tracking-service")
public class TrackingServiceIT extends AbstractIT {

    @Autowired
    private TrackingService trackingService;

    @Test
    @DisplayName("Should Register TrackedItem Test")
    public void shouldRegisterTrackedItem() {

        TrackedItemRequestDto requestDto = TrackedItemRequestDto.builder()
                .url("https://rozetka.com.ua/ua/378359865/p378359865/")
                .listenerId(1L)
                .build();

        TrackedItem trackedItem = trackingService.registerTrackedItem(requestDto);

        assertThat(trackedItem).isNotNull();
        assertThat(trackedItem.getId()).isNotNull();
        assertThat(trackedItem.getShop()).isNotNull();
        assertThat(trackedItem.getShop().getDomain()).isEqualTo("rozetka.com.ua");
        assertThat(trackedItem.getProduct()).isNotNull();
        assertThat(trackedItem.getProduct().getId()).isNotNull();
        assertThat(trackedItem.getListeners()).isNotNull();
        assertThat(trackedItem.getListeners()).isNotEmpty();
        assertThat(trackedItem.getListeners().getFirst().getId()).isEqualTo(1L);
    }

}
