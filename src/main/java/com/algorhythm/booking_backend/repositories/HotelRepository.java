package com.algorhythm.booking_backend.repositories;

import com.algorhythm.booking_backend.entities.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {
}
