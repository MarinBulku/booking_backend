package com.algorhythm.booking_backend.repositories;

import com.algorhythm.booking_backend.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findByUser_UserId(Integer userId);
}
