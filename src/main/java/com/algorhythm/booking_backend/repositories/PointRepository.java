package com.algorhythm.booking_backend.repositories;

import com.algorhythm.booking_backend.entities.Points;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Points, Integer> {
}
