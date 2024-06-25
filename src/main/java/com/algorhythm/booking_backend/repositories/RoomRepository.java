package com.algorhythm.booking_backend.repositories;

import com.algorhythm.booking_backend.dataproviders.Booking.RoomSearchRequest;
import com.algorhythm.booking_backend.entities.Hotel;
import com.algorhythm.booking_backend.entities.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;

public interface RoomRepository extends JpaRepository<Room, Integer> {

    @Query("SELECT r FROM Room r " +
            "WHERE r.hotel = :hotel " +
            "AND r.adultsCapacity = :adultsCapacity " +
            "AND r.kidsCapacity = :kidsCapacity " +
            "AND NOT EXISTS (" +
            "   SELECT b FROM Booking b " +
            "   WHERE b.room = r " +
            "   AND (" +
            "       (b.startDate <= :startDate AND :startDate <= b.endDate) OR " +
            "       (b.startDate <= :endDate AND :endDate <= b.endDate) OR " +
            "       (:startDate <= b.startDate AND b.startDate <= :endDate)" +
            "   )" +
            ")")
    Page<Room> findAvailableRooms(
            Hotel hotel,
            LocalDate startDate,
            LocalDate endDate,
            Integer adultsCapacity,
            Integer kidsCapacity,
            Pageable pageable
    );
}
