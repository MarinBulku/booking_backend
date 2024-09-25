package com.algorhythm.booking_backend.application.service.interfaces;

import com.algorhythm.booking_backend.adapter.in.models.booking.BookingHistoryDto;
import com.algorhythm.booking_backend.core.entities.Booking;

import java.util.List;

public interface BookingService {

    //Booking Service method interfaces
    List<Booking> findAll();

    List<BookingHistoryDto> findAllBookingsFromUser(Integer userId);

    Booking findById(Integer bookingId);

    boolean cancelBooking(Integer bookingId, Integer userId);

}