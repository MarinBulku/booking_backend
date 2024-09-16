package com.algorhythm.booking_backend.dataproviders.repositories;

import com.algorhythm.booking_backend.dataproviders.entities.Points;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Points, Integer> {

    /*
    * findByRoom_RoomId(Integer roomId)
    * roomId - ID of room that we must search points for
    *
    * Returns a list of Points entity for a specific room
    * */
    List<Points> findByRoom_RoomId(Integer roomId);
}
