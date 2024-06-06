package com.algorhythm.booking_backend.repositories;

import com.algorhythm.booking_backend.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}
