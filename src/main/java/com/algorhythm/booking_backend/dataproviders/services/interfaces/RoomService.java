package com.algorhythm.booking_backend.dataproviders.services.interfaces;

import com.algorhythm.booking_backend.dataproviders.dtos.Booking.BookingRequest;
import com.algorhythm.booking_backend.dataproviders.dtos.Booking.RoomSearchRequest;
import com.algorhythm.booking_backend.dataproviders.dtos.Room.AvailableRoomDto;
import com.algorhythm.booking_backend.dataproviders.dtos.Room.RoomCreationRequest;
import com.algorhythm.booking_backend.dataproviders.entities.Room;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RoomService {

    //Room Service method interfaces
    List<Room> findAll();

    Page<AvailableRoomDto> findAvailableRoomsToBook2(RoomSearchRequest request, Integer pageNo);

    Room findById(Integer id);

    boolean existsById(Integer id);

    boolean addRoom(RoomCreationRequest request);

    boolean removeRoom(Integer idOfRoomToBeRemoved);

    boolean bookRoom(BookingRequest request);
}
