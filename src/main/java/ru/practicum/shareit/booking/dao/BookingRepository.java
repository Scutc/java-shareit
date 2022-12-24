package ru.practicum.shareit.booking.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findBookingById(Long bookingId);

    Booking findBookingsByIdAndBooker_Id(Long bookingId, Long userId);

    @Query("SELECT b FROM Booking b WHERE b.booker.id =?1 ORDER BY b.id DESC")
    List<Booking> getBookingsByBooker(Long userId);

    @Query("SELECT b FROM Booking b WHERE b.item.ownerId =?1 ORDER BY b.id DESC")
    List<Booking> getBookingByOwner(Long ownerId);
}
