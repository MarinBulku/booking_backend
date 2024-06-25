package com.algorhythm.booking_backend.services.interfaces;

import com.algorhythm.booking_backend.dataproviders.Booking.RoomSearchRequest;
import com.algorhythm.booking_backend.dataproviders.Room.AvailableRoomDto;
import com.algorhythm.booking_backend.dataproviders.Room.RoomCreationRequest;
import com.algorhythm.booking_backend.entities.Room;
import org.springframework.data.domain.Page;

import java.util.Date;
import java.util.List;

public interface RoomService {

    List<Room> findAll();

    Page<AvailableRoomDto> findAvailableRoomsToBook(RoomSearchRequest request, Integer pageNo);

    Room findById(Integer id);

    boolean existsById(Integer id);

    boolean addRoom(RoomCreationRequest request);

    boolean removeRoom(Integer idOfRoomToBeRemoved);
}
