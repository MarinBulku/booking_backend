package com.algorhythm.booking_backend.repositories;

import com.algorhythm.booking_backend.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
}
