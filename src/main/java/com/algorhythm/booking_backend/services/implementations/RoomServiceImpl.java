package com.algorhythm.booking_backend.services.implementations;

import com.algorhythm.booking_backend.dataproviders.Room.RoomCreationRequest;
import com.algorhythm.booking_backend.entities.Hotel;
import com.algorhythm.booking_backend.entities.Room;
import com.algorhythm.booking_backend.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.exceptions.ImageTooLargeException;
import com.algorhythm.booking_backend.exceptions.IncorrectFileTypeException;
import com.algorhythm.booking_backend.repositories.HotelRepository;
import com.algorhythm.booking_backend.repositories.RoomRepository;
import com.algorhythm.booking_backend.services.interfaces.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
    public boolean addRoom(RoomCreationRequest request) {

        Optional<Hotel> optional = hotelRepository.findById(request.getHotelId());
        if (optional.isEmpty()) throw new EntityNotFoundException("No hotel found with this id: " + request.getHotelId());

        MultipartFile file = request.getRoomImage();
        if (file.getSize() > 102400)
            throw new ImageTooLargeException("Image size larger than 100KB: " + file.getSize());
        else if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            throw new IncorrectFileTypeException("File type provided not image: " +file.getContentType());
        }

        String FOLDER_PATH = "C:\\Users\\User\\git\\booking_backend\\src\\main\\resources\\roomImages\\";
        String filePath = FOLDER_PATH + file.getOriginalFilename();
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            return false;
        }

        Room newRoom = Room.builder()
                .roomName(request.getRoomName())
                .hotel(optional.get())
                .adultsCapacity(request.getAdultCapacity())
                .kidsCapacity(request.getKidCapacity())
                .price(request.getPrice())
                .description(request.getDescription())
                .roomImagePath(filePath)
                .build();

        roomRepository.save(newRoom);
        return true;
    }

    @Override
    public boolean removeRoom(Integer idOfRoomToBeRemoved) {
        if (!roomRepository.existsById(idOfRoomToBeRemoved)) throw new EntityNotFoundException("No room with this id: " + idOfRoomToBeRemoved);
        String pathOfImageToDelete = roomRepository.findById(idOfRoomToBeRemoved).get().getRoomImagePath();
        File file = new File(pathOfImageToDelete);

        if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                return false;
            }
        } else {
            throw new EntityNotFoundException("File not found: " + pathOfImageToDelete);
        }
        roomRepository.deleteById(idOfRoomToBeRemoved);
        return true;
    }
}
