package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface IBookingService {
    Booking createBooking(Long userId, BookingDto bookingDto);

    Booking updateBookingStatus(Long userId, Long bookingId, Boolean newStatus);

    List<Booking> getBookingsByUser(Long userId);

    Booking getBookingById(Long bookingId);

    Booking getBookingByIdAndUser(Long bookingId, Long userId);

    List<Booking> getBookingByOwner(Long ownerId);
}
