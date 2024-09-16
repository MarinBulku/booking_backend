package com.algorhythm.booking_backend.dataproviders.repositories;

import com.algorhythm.booking_backend.dataproviders.entities.DiscountDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DiscountDateRepository extends JpaRepository<DiscountDate, Integer> {

    /*
    * findByDiscountDateBetweenAndRoom_RoomId(LocalDate startDate, LocalDate endDate, Integer roomId)
    * startDate, endDate - Date range to search discount at
    * roomId - Room that the discounts should be searched on
    *
    * Searches for any discount dates on a specific room on a date range
    * It returns as a discount date entity list if found, else an empty list
    * */
    List<DiscountDate> findByDiscountDateBetweenAndRoom_RoomId(LocalDate startDate, LocalDate endDate, Integer roomId);
}
