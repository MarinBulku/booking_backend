package com.algorhythm.booking_backend.application.service.interfaces;

import com.algorhythm.booking_backend.adapter.in.models.booking.BookingDto;
import com.algorhythm.booking_backend.adapter.in.models.booking.BookingHistoryDto;

import java.util.List;

public interface BookingService {

    //Booking Service method interfaces
    List<BookingDto> findAll();

    List<BookingHistoryDto> findAllBookingsFromUser(Integer userId);

    BookingDto findById(Integer bookingId);

    boolean cancelBooking(Integer bookingId, Integer userId);

}