package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.dao.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoXl;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.IItemService;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.IUserService;
import ru.practicum.shareit.user.service.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IItemService itemService;
    private final IUserService userService;

    @Transactional
    @Override
    public Booking createBooking(Long userId, BookingDto bookingDto) {
        UserDto userDto = userService.getUserById(userId);
        ItemDtoXl itemDto = itemService.getItemById(bookingDto.getItemId());
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
        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public Booking updateBookingStatus(Long userId, Long bookingId, Boolean newStatus) {
        Booking booking = bookingRepository.findBookingById(bookingId, userId);
        if (booking == null) {
            throw new NotAllowedToChangeException("Бронирование соответствующее условию запроса не найдено");
        }
        booking = bookingRepository.findBookingByIdOwner(bookingId, userId);
        if (booking == null) {
            throw new EntityNotFoundException("У пользователя " + userId +
                    " Отсутствуют права на изменение статуса бронирования " + bookingId);
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED) && newStatus)  {
            throw new NotAllowedToChangeException("Бронирование нельзя подтвердить повторно!");
        }
        if (newStatus) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    private boolean checkBookingDates(BookingDto bookingDto) {
        return (bookingDto.getStart().isBefore(bookingDto.getEnd()) && bookingDto.getStart()
                                                                                 .isAfter(LocalDateTime.now()));
    }

    @Override
    public Booking getBookingById(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findBookingById(bookingId, userId);
        if (booking == null) {
            throw new EntityNotFoundException("Брониронивание с ID " + bookingId + " не найдено");
        }
        return booking;
    }

    @Override
    public List<Booking> getBookingsByUser(Long userId, BookingStatus state) throws UserNotFoundException {
        userService.getUserById(userId);
        if (state == null || state == BookingStatus.ALL) {
            return bookingRepository.getBookingsByBooker(userId);
        }
        switch (state) {
            case FUTURE:
                return bookingRepository.getBookingsByBookerFuture(userId);
            case WAITING:
            case REJECTED:
                return bookingRepository.getBookingsByBookerWithState(userId, state);
            default:
                throw new UnsupportedStatusException();
        }
    }

    @Override
    public List<Booking> getBookingByOwner(Long ownerId, BookingStatus state) throws UserNotFoundException {
        userService.getUserById(ownerId);
        if (state == null || state == BookingStatus.ALL) {
            return bookingRepository.getBookingByOwner(ownerId);
        }
        switch (state) {
            case FUTURE:
                return bookingRepository.getBookingByOwnerFuture(ownerId);
            case WAITING:
            case REJECTED:
                return bookingRepository.getBookingByOwnerWithState(ownerId, state);
            default:
                throw new UnsupportedStatusException();
        }
    }

    @Override
    public List<Booking> getBookingByItem_Id(Long itemId) {
        return bookingRepository.getBookingByItem_IdOrderByStartAsc(itemId);

    }
}
