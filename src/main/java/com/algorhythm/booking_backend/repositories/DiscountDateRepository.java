package com.algorhythm.booking_backend.repositories;

import com.algorhythm.booking_backend.entities.DiscountDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DiscountDateRepository extends JpaRepository<DiscountDate, Integer> {

    List<DiscountDate> findByDiscountDateBeforeAndDiscountDateAfterAndRoom_RoomId(LocalDate startDate, LocalDate endDate, Integer roomId);
}
