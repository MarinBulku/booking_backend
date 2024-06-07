package com.algorhythm.booking_backend.services.interfaces;

import com.algorhythm.booking_backend.entities.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingService {

    List<Booking> findAll();

    List<Booking> findAllBookingsFromUser(Integer userId);

    Booking findById(Integer bookingId);

    void cancelBooking(Integer bookingId);

    Booking bookRoom(Integer roomId, LocalDate startDate, LocalDate endDate, Integer userId, Double price);
}