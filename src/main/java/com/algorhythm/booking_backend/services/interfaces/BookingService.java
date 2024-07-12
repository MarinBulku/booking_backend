package com.algorhythm.booking_backend.services.interfaces;

import com.algorhythm.booking_backend.dataproviders.Booking.BookingHistoryDto;
import com.algorhythm.booking_backend.entities.Booking;

import java.util.List;

public interface BookingService {

    //Booking Service method interfaces
    List<Booking> findAll();

    List<BookingHistoryDto> findAllBookingsFromUser(Integer userId);

    Booking findById(Integer bookingId);

    boolean cancelBooking(Integer bookingId, Integer userId);

}