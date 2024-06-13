package com.algorhythm.booking_backend.services.implementations;

import com.algorhythm.booking_backend.dataproviders.Hotel.HotelCreationRequest;
import com.algorhythm.booking_backend.dataproviders.Hotel.HotelDTO;
import com.algorhythm.booking_backend.entities.Hotel;
import com.algorhythm.booking_backend.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.exceptions.ImageTooLargeException;
import com.algorhythm.booking_backend.exceptions.IncorrectFileTypeException;
import com.algorhythm.booking_backend.repositories.HotelRepository;
import com.algorhythm.booking_backend.services.interfaces.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    @Override
    public List<Hotel> findAll() {
        List<Hotel> allHotels = hotelRepository.findAll();

        if (allHotels.isEmpty()) return new ArrayList<>();

        return allHotels;
    }

    @Override
    public List<HotelDTO> allHotelDtos() {
        List<Hotel> allHotels = findAll();
        ArrayList<HotelDTO> allHotelDtos = new ArrayList<>();

        for (Hotel h:
             allHotels) {
            HotelDTO hotelDTO = HotelDTO.builder()
                    .hotelId(h.getHotelId())
                    .hotelName(h.getHotelName())
                    .build();
            allHotelDtos.add(hotelDTO);
        }

        return allHotelDtos;
    }

    @Override
    public Hotel findById(Integer id) {
        Optional<Hotel> optional = hotelRepository.findById(id);

        if (optional.isEmpty()) throw new EntityNotFoundException("No hotel with this id: " + id);

        return optional.get();
    }

    @Override
    public boolean existsById(Integer id) {
        return hotelRepository.existsById(id);
    }

    @Override
    public boolean addHotel(HotelCreationRequest request) {

        MultipartFile file = request.getHotelImage();
        if (file.getSize() > 102400)
            throw new ImageTooLargeException("Image size larger than 100KB: " + file.getSize());
        else if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            throw new IncorrectFileTypeException("File type provided not image: " +file.getContentType());
        }

        String FOLDER_PATH = "C:\\Users\\User\\git\\booking_backend\\src\\main\\resources\\businessImages\\";
        String filePath = FOLDER_PATH + file.getOriginalFilename();
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            return false;
        }

        Hotel newHotel = Hotel.builder()
                .hotelName(request.getHotelName())
                .hotelImagePath(filePath)
                .freeParking(request.isFreeParking())
                .freeWiFi(request.isFreeWiFi())
                .freePool(request.isFreePool())
                .freeBreakfast(request.isFreeBreakfast())
                .build();

        hotelRepository.save(newHotel);
        return true;
    }

    @Override
    public boolean removeHotel(Integer idOfHotelToBeRemoved) {
        if (!hotelRepository.existsById(idOfHotelToBeRemoved)) throw new EntityNotFoundException("No hotel with this id: " + idOfHotelToBeRemoved);

        String pathOfImageToDelete = hotelRepository.findById(idOfHotelToBeRemoved).get().getHotelImagePath();
        File file = new File(pathOfImageToDelete);

        if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                return false;
            }
        } else {
            throw new EntityNotFoundException("File not found: " + pathOfImageToDelete);
        }
        hotelRepository.deleteById(idOfHotelToBeRemoved);
        return true;
    }
}
