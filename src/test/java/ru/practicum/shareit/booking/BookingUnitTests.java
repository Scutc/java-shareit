package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.service.IBookingService;
import ru.practicum.shareit.exception.UnsupportedStatusException;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class BookingUnitTests {
    @Mock
    IBookingService bookingService;

    @Test
    void testGetBookingsByUser() {
        bookingService.getBookingsByUser(1L, "ALL", null, null);
        Mockito
                .when(bookingService.getBookingsByUser(Mockito.anyLong(), Mockito.anyString(),
                        Mockito.anyInt(), Mockito.anyInt()))
                .thenAnswer(invocationOnMock -> {
                    String state = invocationOnMock.getArgument(1, String.class);
                    switch (state) {
                        case "ALL":
                        case "FUTURE":
                        case "CURRENT":
                        case "PAST":
                        case "WAITING":
                        case "REJECTED":
                            return new ArrayList<BookingDtoResponse>();
                        default:
                            throw new UnsupportedStatusException(state);
                    }
                });
        assertThrows(UnsupportedStatusException.class, () -> bookingService.getBookingsByUser(1L, "OTHER",
                1, 2));
    }
}
