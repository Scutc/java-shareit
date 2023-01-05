package ru.practicum.shareit.booking;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.practicum.shareit.booking.service.IBookingService;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class BookingControllerTestConfig {
    @Bean
    public IBookingService bookingService() {
        return mock(IBookingService.class);
    }
}
