package com.algorhythm.booking_backend.repositories;

import com.algorhythm.booking_backend.dataproviders.Booking.BookingHistoryDto;
import com.algorhythm.booking_backend.entities.Booking;
import com.algorhythm.booking_backend.entities.Room;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    /*
    * findByUser_UserId(Integer userId, Pageable pageable)
    * userId - User to find its booking history
    * pageable - To get results as a page
    *
    * Returns the page requested of user's booking
    * */
    @Query("SELECT NEW com.algorhythm.booking_backend.dataproviders.Booking.BookingHistoryDto(b.bookingId, b.room.hotel.hotelName, b.pricePaid, b.startDate, b.status) " +
            "FROM Booking b WHERE b.user.userId = :userId")
    List<BookingHistoryDto> findByUser_UserId(Integer userId);

    /*
    * updateCompletedBookings(LocalDate today)
    *
    * Updates all active bookings which have ended, and sets their status to 'COMPLETE'
    * */
    @Modifying
    @Transactional
    @Query("UPDATE Booking b SET b.status = 'COMPLETE' WHERE b.status = 'ACTIVE' AND b.endDate < :today")
    void updateCompletedBookings(LocalDate today);

    /*
    * isThereABooking(Room room, LocalDate startDate, LocalDate endDate)
    * room - Room to find bookings on
    * startDate, endDate - Range of date to search for a booking
    *
    * Returns a booking made on that room between the 2 dates, else returns null
    * */
    @Query("SELECT B FROM Booking B WHERE B.room = :room AND ((B.startDate BETWEEN :startDate AND :endDate) OR (B.endDate BETWEEN :startDate AND :endDate)) AND B.status != 'CANCELLED'")
    Optional<Booking> isThereABooking(Room room, LocalDate startDate, LocalDate endDate);
}
