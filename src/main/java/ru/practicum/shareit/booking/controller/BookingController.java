package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.IBookingService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/bookings")
@Validated
@Slf4j
@RequiredArgsConstructor
public class BookingController {

    private final IBookingService bookingService;

    @PostMapping
    public Booking createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @Valid @RequestBody BookingDto bookingDto) {
        log.info("Поступил запрос на создание бронирования от пользователя ID = " + userId);
        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public Booking updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                 @PathVariable Long bookingId,
                                 @RequestParam(required = false) Boolean approved) {
        log.info("Поступил запрос на обновление бронирования ID = " + bookingId + " от пользователя" + userId);
        return bookingService.updateBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public Booking getBookingById(@PathVariable Long bookingId) {
        log.info("Поступил запрос на получение бронирования с ID = " + bookingId);
        return bookingService.getBookingById(bookingId);
    }

    @GetMapping("/owner")
    public List<Booking> getBookingByUser(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                          @RequestParam(required = false) BookingStatus state) {
        log.info("Поступил запрос на получение списка бронирований для пользователя " + ownerId);
        return bookingService.getBookingByOwner(ownerId, state);
    }

    @GetMapping()
    public List<Booking> getAllBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                       @RequestParam(required = false) BookingStatus state) {
        return bookingService.getBookingsByUser(userId, state);
    }
}
