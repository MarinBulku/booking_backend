package com.algorhythm.booking_backend.dataproviders.services.interfaces;

import com.algorhythm.booking_backend.dataproviders.dtos.booking.BookingRequest;
import com.algorhythm.booking_backend.dataproviders.dtos.booking.RoomSearchRequest;
import com.algorhythm.booking_backend.dataproviders.dtos.room.AvailableRoomDto;
import com.algorhythm.booking_backend.dataproviders.dtos.room.RoomCreationRequest;
import com.algorhythm.booking_backend.core.entities.Room;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface RoomService {

    //Room Service method interfaces
    List<Room> findAll();

    Page<AvailableRoomDto> findAvailableRoomsToBook2(RoomSearchRequest request, Integer pageNo);

    Room findById(Integer id);

    boolean existsById(Integer id);

    boolean addRoom(RoomCreationRequest request);

    boolean removeRoom(Integer idOfRoomToBeRemoved) throws IOException;

    boolean bookRoom(BookingRequest request);
}
