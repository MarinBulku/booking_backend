package com.algorhythm.booking_backend.services.implementations;

import com.algorhythm.booking_backend.dataproviders.Booking.BookingRequest;
import com.algorhythm.booking_backend.dataproviders.Hotel.AvailableHotelDto;
import com.algorhythm.booking_backend.dataproviders.Hotel.HotelCreationRequest;
import com.algorhythm.booking_backend.dataproviders.Hotel.HotelDTO;
import com.algorhythm.booking_backend.entities.Hotel;
import com.algorhythm.booking_backend.entities.User;
import com.algorhythm.booking_backend.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.exceptions.ImageTooLargeException;
import com.algorhythm.booking_backend.exceptions.IncorrectFileTypeException;
import com.algorhythm.booking_backend.repositories.HotelRepository;
import com.algorhythm.booking_backend.repositories.UserRepository;
import com.algorhythm.booking_backend.services.interfaces.HotelService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;

    @Override
    public List<Hotel> findAll() {
        List<Hotel> allHotels = hotelRepository.findAll();

        if (allHotels.isEmpty()) return new ArrayList<>();

        return allHotels;
    }

    @Override
    public List<HotelDTO> allHotelDtos() {
        List<Hotel> allHotels = findAll();
        return getHotelDTOS(allHotels);
    }

    @Override
    public List<HotelDTO> allHotelDtosByOwner(Integer ownerId) {
        Optional<User> owner = userRepository.findById(ownerId);

        if (owner.isEmpty()) throw new EntityNotFoundException("No user with this ID: " + ownerId);

        List<Hotel> allHotelsByOwner = hotelRepository.findHotelsByOwner(owner.get());
        return getHotelDTOS(allHotelsByOwner);
    }

   @Override
   public Page<AvailableHotelDto> findAllAvailableHotels(BookingRequest request, Integer pageNo){
        if (!request.getCheckInDate().isBefore(request.getCheckOutDate()))
            throw new IllegalArgumentException("Check in date should only be before check out date");
        final int pageSize = 18;

       Pageable pageable = PageRequest.of(pageNo, pageSize);

       Page<Hotel> availableHotels = hotelRepository.findAvailableHotels(
                request.getCheckInDate(),
                request.getCheckOutDate(),
                request.getAdultsNumber(),
                request.getKidsNumber(),
                pageable
        );

       List<AvailableHotelDto> availableHotelDtoList = availableHotels.stream().map(h -> {
           try {
               String hotelImage = Base64.getEncoder().encodeToString(
                       FileUtils.readFileToByteArray(new File(h.getHotelImagePath()))
               );
               return AvailableHotelDto.builder()
                       .hotelId(h.getHotelId())
                       .hotelName(h.getHotelName())
                       .freeBreakfast(h.isFreeBreakfast())
                       .freeWiFi(h.isFreeWiFi())
                       .freeParking(h.isFreeParking())
                       .freePool(h.isFreePool())
                       .hotelImage(hotelImage)
                       .build();
           } catch (IOException e) {
               throw new RuntimeException("Failed to read hotel image file", e);
           }
       }).collect(Collectors.toList());

       return new PageImpl<>(availableHotelDtoList, pageable, availableHotelDtoList.size());
    }

    private List<HotelDTO> getHotelDTOS(List<Hotel> allHotelsByOwner) {
        ArrayList<HotelDTO> allHotelsByOwnerDtos = new ArrayList<>();

        for (Hotel h:
                allHotelsByOwner) {
            HotelDTO hotelDTO = HotelDTO.builder()
                    .hotelId(h.getHotelId())
                    .hotelName(h.getHotelName())
                    .build();
            allHotelsByOwnerDtos.add(hotelDTO);
        }

        return allHotelsByOwnerDtos;
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

        Optional<User> owner = userRepository.findById(request.getOwnerId());
        if (owner.isEmpty()) throw new EntityNotFoundException("No user found with this ID: " + request.getOwnerId());

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
                .owner(owner.get())
                .hotelName(request.getHotelName())
                .hotelImagePath(filePath)
                .freeParking(Boolean.getBoolean(request.getFreeParking()))
                .freeWiFi(Boolean.getBoolean(request.getFreeWiFi()))
                .freePool(Boolean.getBoolean(request.getFreePool()))
                .freeBreakfast(Boolean.getBoolean(request.getFreeBreakfast()))
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
