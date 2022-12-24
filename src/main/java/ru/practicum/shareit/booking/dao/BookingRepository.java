package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findBookingById(Long bookingId);

    Booking findBookingsByIdAndBooker_Id(Long bookingId, Long userId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id =?1 ORDER BY b.id DESC")
    List<Booking> getBookingsByBooker(Long userId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id =?1 AND b.status = ?2 ORDER BY b.id DESC")
    List<Booking> getBookingsByBookerWithState(Long userId, BookingStatus state);

    @Query("SELECT b FROM Booking b WHERE b.booker.id =?1 AND b.end > current_timestamp ORDER BY b.id DESC")
    List<Booking> getBookingsByBookerFuture(Long userId);

    @Query("SELECT b FROM Booking b WHERE b.item.ownerId =?1 ORDER BY b.id DESC")
    List<Booking> getBookingByOwner(Long ownerId);

    @Query("SELECT b FROM Booking b WHERE b.item.ownerId =?1 AND b.status = ?2 ORDER BY b.id DESC")
    List<Booking> getBookingByOwnerWithState(Long ownerId, BookingStatus status);

    @Query("SELECT b FROM Booking b WHERE b.item.ownerId =?1 AND b.end > current_timestamp ORDER BY b.id DESC")
    List<Booking> getBookingByOwnerFuture(Long ownerId);
}
