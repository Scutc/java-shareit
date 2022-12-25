package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

public interface IBookingService {
    Booking createBooking(Long userId, BookingDto bookingDto);

    Booking updateBookingStatus(Long userId, Long bookingId, Boolean newStatus);

    List<Booking> getBookingsByUser(Long userId, BookingStatus state);

    Booking getBookingById(Long bookingId, Long userId);

    List<Booking> getBookingByOwner(Long ownerId, BookingStatus state);

    List<Booking> getBookingByItem_Id(Long itemId);
}
