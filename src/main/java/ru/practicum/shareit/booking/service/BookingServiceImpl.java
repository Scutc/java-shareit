package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dto.ItemDtoResponse;
import ru.practicum.shareit.item.service.IItemService;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.IUserService;
import ru.practicum.shareit.user.service.UserMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IItemService itemService;
    private final IUserService userService;

    @Transactional
    @Override
    public BookingDtoResponse createBooking(Long userId, BookingDto bookingDto) {
        UserDto userDto = userService.getUserById(userId);
        ItemDtoResponse itemDto = itemService.getItemById(bookingDto.getItemId(), userId);
        if (!itemDto.getAvailable()) {
            throw new NotAvailableException(bookingDto.getItemId());
        }
        if (!checkBookingDates(bookingDto)) {
            throw new NotValidDateException();
        }
        if (itemDto.getUserId() == userId) {
            throw new EntityNotFoundException("Нельзя забронировать собственную вещь!");
        }
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(UserMapper.toUser(userDto));
        booking.setItem(ItemMapper.toItem(itemDto));
        booking = bookingRepository.save(booking);
        return BookingMapper.toBookingDtoResponse(booking);
    }

    @Transactional
    @Override
    public BookingDtoResponse updateBookingStatus(Long userId, Long bookingId, Boolean newStatus) {
        Booking booking = bookingRepository.findBookingById(bookingId, userId);
        if (booking == null) {
            throw new NotAllowedToChangeException("Бронирование соответствующее условию запроса не найдено");
        }
        booking = bookingRepository.findBookingByIdOwner(bookingId, userId);
        if (booking == null) {
            throw new EntityNotFoundException("У пользователя " + userId +
                    " Отсутствуют права на изменение статуса бронирования " + bookingId);
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED) && newStatus) {
            throw new NotAllowedToChangeException("Бронирование нельзя подтвердить повторно!");
        }
        if (newStatus) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.toBookingDtoResponse(bookingRepository.save(booking));
    }

    private boolean checkBookingDates(BookingDto bookingDto) {
        return (bookingDto.getStart().isBefore(bookingDto.getEnd()) && bookingDto.getStart()
                                                                                 .isAfter(LocalDateTime.now()));
    }

    @Override
    public BookingDtoResponse getBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findBookingById(bookingId, userId);
        if (booking == null) {
            throw new EntityNotFoundException("Брониронивание с ID " + bookingId + " не найдено");
        }
        return BookingMapper.toBookingDtoResponse(booking);
    }

    @Override
    public List<BookingDtoResponse> getBookingsByUser(Long userId, String state) throws UserNotFoundException {
        userService.getUserById(userId);
        List<Booking> bookings = new ArrayList<>();
        if (state == null || state.equals("ALL")) {
            return makeListOfBookingDtoResponse(bookingRepository.getBookingsByBooker(userId));
        }
        switch (state) {
            case "FUTURE":
                return makeListOfBookingDtoResponse(bookingRepository.getBookingsByBookerFuture(userId));
            case "CURRENT":
                return makeListOfBookingDtoResponse(bookingRepository.getBookingsByBookerCurrent(userId));
            case "PAST":
                return makeListOfBookingDtoResponse(bookingRepository.getBookingsByBookerPast(userId));
            case "WAITING":
            case "REJECTED":
                return makeListOfBookingDtoResponse(bookingRepository
                        .getBookingsByBookerWithState(userId, BookingStatus.valueOf(state)));
            default:
                throw new UnsupportedStatusException(state);
        }
    }

    private List<BookingDtoResponse> makeListOfBookingDtoResponse(List<Booking> bookings) {
        return bookings.stream()
                       .map(BookingMapper::toBookingDtoResponse)
                       .collect(Collectors.toList());
    }

    @Override
    public List<BookingDtoResponse> getBookingByOwner(Long ownerId, String state) throws UserNotFoundException {
        userService.getUserById(ownerId);
        if (state == null || state.equals("ALL")) {
            return makeListOfBookingDtoResponse(bookingRepository.getBookingByOwner(ownerId));
        }
        switch (state) {
            case "FUTURE":
                return makeListOfBookingDtoResponse(bookingRepository.getBookingByOwnerFuture(ownerId));
            case "CURRENT":
                return makeListOfBookingDtoResponse(bookingRepository.getBookingsByOwnerCurrent(ownerId));
            case "PAST":
                return makeListOfBookingDtoResponse(bookingRepository.getBookingsByOwnerPast(ownerId));
            case "WAITING":
            case "REJECTED":
                return makeListOfBookingDtoResponse(bookingRepository
                        .getBookingByOwnerWithState(ownerId, BookingStatus.valueOf(state)));
            default:
                throw new UnsupportedStatusException(state);
        }
    }
}
