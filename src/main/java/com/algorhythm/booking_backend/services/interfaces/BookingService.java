package com.algorhythm.booking_backend.services.interfaces;

import com.algorhythm.booking_backend.dataproviders.Booking.BookingHistoryDto;
import com.algorhythm.booking_backend.entities.Booking;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    List<Booking> findAll();

    Page<BookingHistoryDto> findAllBookingsFromUser(Integer userId, Integer pageSize, Integer pageNo, String orderBy);

    Booking findById(Integer bookingId);

    boolean cancelBooking(Integer bookingId, Integer userId);

    Booking bookRoom(Integer roomId, LocalDate startDate, LocalDate endDate, Integer userId, Double price);
}