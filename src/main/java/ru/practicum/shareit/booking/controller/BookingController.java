package ru.practicum.shareit.booking.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
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
    public BookingDtoResponse createBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @Valid @RequestBody BookingDto bookingDto) {
        log.info("Поступил запрос на создание бронирования от пользователя ID = " + userId);
        return bookingService.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoResponse updateBooking(@RequestHeader("X-Sharer-User-Id") Long userId,
                                            @PathVariable Long bookingId,
                                            @RequestParam(required = false) Boolean approved) {
        log.info("Поступил запрос на обновление бронирования ID = " + bookingId + " от пользователя" + userId);
        return bookingService.updateBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoResponse getBookingById(@PathVariable Long bookingId,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Поступил запрос на получение бронирования с ID = " + bookingId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping("/owner")
    public List<BookingDtoResponse> getBookingByOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                      @RequestParam(required = false) String state) {
        log.info("Поступил запрос на получение списка бронирований для пользователя " + ownerId);
        if (state == null) {
            state = "ALL";
        }
        return bookingService.getBookingByOwner(ownerId, state);
    }

    @GetMapping()
    public List<BookingDtoResponse> getAllBookings(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                  @RequestParam(required = false) String state) {
        log.info("Поступил запрос на получение всех бронирований");
        if (state == null) {
            state = "ALL";
        }
        return bookingService.getBookingsByUser(userId, state);
    }
}
