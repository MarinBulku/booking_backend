package com.algorhythm.booking_backend.repositories;

import com.algorhythm.booking_backend.entities.Points;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Points, Integer> {

    List<Points> findByRoom_RoomId(Integer roomId);
}
