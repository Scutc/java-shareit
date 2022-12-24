package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dao.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.CustomSecurityException;
import ru.practicum.shareit.exception.NotAvailableException;
import ru.practicum.shareit.exception.NotValidDateException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.IItemService;
import ru.practicum.shareit.item.service.ItemMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.IUserService;
import ru.practicum.shareit.user.service.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.shareit.booking.service.BookingMapper.*;

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
        ItemDto itemDto = itemService.getItemById(bookingDto.getItemId());
        if (!itemDto.getAvailable()) {
            throw new NotAvailableException(bookingDto.getItemId());
        }
        if (!checkBookingDates(bookingDto)) {
            throw new NotValidDateException();
        }
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(UserMapper.toUser(userDto));
        booking.setItem(ItemMapper.toItem(itemDto));
        return bookingRepository.save(booking);
    }

    @Transactional
    @Override
    public Booking updateBookingStatus(Long userId, Long bookingId, Boolean newStatus) {
        Booking booking = bookingRepository.findBookingById(bookingId);
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
    public Booking getBookingByIdAndUser(Long bookingId, Long userId) {
        return bookingRepository.findBookingsByIdAndBooker_Id(bookingId, userId);
    }

    @Override
    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findBookingById(bookingId);
    }

    @Override
    public List<Booking> getBookingsByUser(Long userId) throws UserNotFoundException {
        userService.getUserById(userId);
        return bookingRepository.getBookingsByBooker(userId);
    }

    @Override
    public List<Booking> getBookingByOwner(Long ownerId) throws UserNotFoundException{
        userService.getUserById(ownerId);
        return bookingRepository.getBookingByOwner(ownerId);
    }
}
