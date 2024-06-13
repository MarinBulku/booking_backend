package com.algorhythm.booking_backend.services.interfaces;

import com.algorhythm.booking_backend.dataproviders.Room.RoomCreationRequest;
import com.algorhythm.booking_backend.entities.Room;

import java.util.Date;
import java.util.List;

public interface RoomService {

    List<Room> findAll();

    List<Room> findAvailableRoomsToBook(Date startDate, Date endDate, Integer adultNumber, Integer kidNumber);

    Room findById(Integer id);

    boolean existsById(Integer id);

    boolean addRoom(RoomCreationRequest request);

    boolean removeRoom(Integer idOfRoomToBeRemoved);
}
