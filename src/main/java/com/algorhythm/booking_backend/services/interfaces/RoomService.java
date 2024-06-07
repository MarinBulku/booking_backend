package com.algorhythm.booking_backend.services.interfaces;

import com.algorhythm.booking_backend.entities.Room;

import java.util.Date;
import java.util.List;

public interface RoomService {

    List<Room> findAll();

    List<Room> findAvailableRoomsToBook(Date startDate, Date endDate, Integer adultNumber, Integer kidNumber);

    Room findById(Integer id);

    boolean existsById(Integer id);

    Room addRoom(String name, Integer hotelId, Integer adultCapacity, Integer kidCapacity, Integer price, String description, String imagePath);

    void removeRoom(Integer idOfRoomToBeRemoved);
}
