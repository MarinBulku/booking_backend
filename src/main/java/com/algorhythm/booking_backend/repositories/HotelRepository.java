package com.algorhythm.booking_backend.repositories;

import com.algorhythm.booking_backend.entities.Hotel;
import com.algorhythm.booking_backend.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Integer> {
    List<Hotel> findHotelsByOwner(User owner);

    @Query("select h from Hotel h " +
            "inner join Room r on r.hotel = h " +
            "where r.adultsCapacity = :adultsNumber and r.kidsCapacity = :kidsNumber " +
            "and not exists (" +
            "   select b from Booking b " +
            "   where b.room = r " +
            "   and (" +
            "       (b.startDate <= :checkInDate and :checkInDate <= b.endDate) or " +
            "       (b.startDate <= :checkOutDate and :checkOutDate <= b.endDate) or " +
            "       (:checkInDate <= b.startDate and b.startDate <= :checkOutDate)" +
            "   )" +
            ")")
    Page<Hotel> findAvailableHotels(
            LocalDate checkInDate,
            LocalDate checkOutDate,
            Integer adultsNumber,
            Integer kidsNumber,
            Pageable pageable);
}
