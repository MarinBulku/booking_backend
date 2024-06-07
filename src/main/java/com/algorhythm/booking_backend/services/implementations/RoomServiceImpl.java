package com.algorhythm.booking_backend.services.implementations;

import com.algorhythm.booking_backend.entities.Hotel;
import com.algorhythm.booking_backend.entities.Room;
import com.algorhythm.booking_backend.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.repositories.HotelRepository;
import com.algorhythm.booking_backend.repositories.RoomRepository;
import com.algorhythm.booking_backend.services.interfaces.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;

    @Override
    public List<Room> findAll() {
        List<Room> allRooms = roomRepository.findAll();
        if (allRooms.isEmpty()) return new ArrayList<>();
        return allRooms;
    }

    @Override
    public List<Room> findAvailableRoomsToBook(Date startDate, Date endDate, Integer adultNumber, Integer kidNumber) {
        return null;
    }

    @Override
    public Room findById(Integer id) {

        Optional<Room> optional = roomRepository.findById(id);
        if (optional.isEmpty()) throw new EntityNotFoundException("No room with this id: " + id);

        return optional.get();
    }

    @Override
    public boolean existsById(Integer id) {
        return roomRepository.existsById(id);
    }

    @Override
    public Room addRoom(String name, Integer hotelId, Integer adultCapacity, Integer kidCapacity, Integer price, String description, String imagePath) {

        Optional<Hotel> optional = hotelRepository.findById(hotelId);
        if (optional.isEmpty()) throw new EntityNotFoundException("No hotel found with this id: " + hotelId);

        Room newRoom = Room.builder()
                .roomName(name)
                .hotel(optional.get())
                .adultsCapacity(adultCapacity)
                .kidsCapacity(kidCapacity)
                .price(price)
                .description(description)
                .roomImagePath(imagePath)
                .build();

        return roomRepository.save(newRoom);
    }

    @Override
    public void removeRoom(Integer idOfRoomToBeRemoved) {
        if (!roomRepository.existsById(idOfRoomToBeRemoved)) throw new EntityNotFoundException("No room with this id: " + idOfRoomToBeRemoved);
        roomRepository.deleteById(idOfRoomToBeRemoved);
    }
}
