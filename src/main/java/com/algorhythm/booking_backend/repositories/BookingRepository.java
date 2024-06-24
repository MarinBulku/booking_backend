package com.algorhythm.booking_backend.repositories;

import com.algorhythm.booking_backend.dataproviders.Booking.BookingHistoryDto;
import com.algorhythm.booking_backend.entities.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query("SELECT NEW com.algorhythm.booking_backend.dataproviders.Booking.BookingHistoryDto(b.room.hotel.hotelName, b.pricePaid, b.startDate, b.status) " +
            "FROM Booking b WHERE b.user.userId = :userId")
    Page<BookingHistoryDto> findByUser_UserId(Integer userId, Pageable pageable);
}
