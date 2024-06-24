package com.algorhythm.booking_backend.repositories;

import com.algorhythm.booking_backend.dataproviders.Booking.BookingHistoryDto;
import com.algorhythm.booking_backend.entities.Booking;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT NEW com.algorhythm.booking_backend.dataproviders.Booking.BookingHistoryDto(b.room.hotel.hotelName, b.pricePaid, b.startDate, b.status) " +
            "FROM Booking b WHERE b.user.userId = :userId")
    Page<BookingHistoryDto> findByUser_UserId(Integer userId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Booking b SET b.status = 'COMPLETE' WHERE b.status = 'ACTIVE' AND b.endDate < :today")
    void updateCompletedBookings(LocalDate today);
}
