package com.algorhythm.booking_backend.application.service.implementations;

import com.algorhythm.booking_backend.adapter.in.models.booking.HotelSearchRequest;
import com.algorhythm.booking_backend.adapter.in.models.hotel.AvailableHotelDto;
import com.algorhythm.booking_backend.adapter.in.models.hotel.HotelCreationRequest;
import com.algorhythm.booking_backend.adapter.in.models.hotel.HotelDTO;
import com.algorhythm.booking_backend.core.entities.Hotel;
import com.algorhythm.booking_backend.core.entities.User;
import com.algorhythm.booking_backend.core.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.core.exceptions.ImageTooLargeException;
import com.algorhythm.booking_backend.core.exceptions.IncorrectFileTypeException;
import com.algorhythm.booking_backend.adapter.out.persistence.HotelRepository;
import com.algorhythm.booking_backend.adapter.out.persistence.UserRepository;
import com.algorhythm.booking_backend.application.service.interfaces.HotelService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {
    //Hotel Service Method implementations
    //HotelRepository to fetch hotel data from repository/database
    //UserRepository to fetch user data from repository/database, mostly for confirmation
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(HotelServiceImpl.class);
    private static final String FOLDER_PATH = "C:\\Users\\ALGORHYTHM\\Documents\\Booking\\booking_backend\\src\\main\\resources\\businessImages\\";

    /*
    * findAll() - No parameters needed
    *
    * Returns a list of ALL Hotel Entities in the repository
    * */
    @Override
    public List<Hotel> findAll() {
        logger.trace("List of all hotels requested, being generated");
        return hotelRepository.findAll();
    }

    /*
     * findAllDtos() - No parameters needed
     *
     * Finds all Hotel Entities, and then parses into HotelDTO to return as a list
     * */
    @Override
    public List<HotelDTO> allHotelDtos() {
        List<Hotel> allHotels = findAll();
        return getHotelDTOS(allHotels);
    }

    /*
    * allHotelDtosByOwner(Integer ownerId)
    * ownerId - ID of the business owner(ADMIN that created the hotel)
    *
    * If no owner exists with this ownerId, EntityNotFoundException is thrown
    *
    * Gets a list of all the hotels owned by this ownerId, then maps
    * objects as a list of HotelDTO to return.
    * */
    @Override
    public List<HotelDTO> allHotelDtosByOwner(Integer ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow( () -> new EntityNotFoundException("No user with this ID: " + ownerId));

        List<Hotel> allHotelsByOwner = hotelRepository.findHotelsByOwner(owner);
        logger.trace("List of all hotels requested by user {}, being generated", ownerId);
        return getHotelDTOS(allHotelsByOwner);
    }

    /*
    * findAllAvailableHotels(HotelSearchRequest request, Integer pageNo)
    * request - The data we need to filter to find available hotels, matching
    * the criteria specified in the request.
    * pageNo - The page number that needs to be sent as a response.
    *
    * If request's start date is not before end date, IllegalArgumentException is thrown
    *
    * After, a page is fetched from repository with the criteria specified.
    *
    * From the page returned we create the list of AvailableHotelDto to return
    * with the correct data needed.
    * */
    @Override
    public Page<AvailableHotelDto> findAllAvailableHotels(HotelSearchRequest request, Integer pageNo) {
        if (request.getCheckInDate().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Check-in date cannot be before today");

        if (!request.getCheckInDate().isBefore(request.getCheckOutDate())) {
            throw new IllegalArgumentException("Check-in date should be before check-out date");
        }

        final int pageSize = 18;
        Pageable pageable = PageRequest.of(pageNo, pageSize);

        Page<Object[]> availableHotelsWithFreeRooms = hotelRepository.findAvailableHotels(
                request.getCheckInDate(),
                request.getCheckOutDate(),
                request.getAdultsNumber(),
                request.getKidsNumber(),
                pageable
        );

        List<AvailableHotelDto> availableHotelDtoList = availableHotelsWithFreeRooms.getContent().stream().map(row -> {
            Hotel h = (Hotel) row[0];
            Long freeRooms = (Long) row[1];

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
                        .roomCount(freeRooms.intValue())
                        .build();
            } catch (IOException e) {
                logger.error("Could not read content from file!!!");
                throw new RuntimeException("Cannot read from file! " + e.getMessage());
            }
        }).toList();
        logger.trace("Page of available hotels requested, being generated");
        return new PageImpl<>(availableHotelDtoList, pageable, availableHotelsWithFreeRooms.getTotalElements());
    }

    /*
    * getHotelDTOS(List<Hotel> hotels)
    * hotels - List of hotels
    *
    * Transforms each Hotel object to HotelDto object, and return the final list
    * */
    private List<HotelDTO> getHotelDTOS(List<Hotel> hotels) {
        ArrayList<HotelDTO> allHotelDtos = new ArrayList<>();

        for (Hotel h:
                hotels) {
            HotelDTO hotelDTO = HotelDTO.builder()
                    .hotelId(h.getHotelId())
                    .hotelName(h.getHotelName())
                    .build();
            allHotelDtos.add(hotelDTO);
        }

        return allHotelDtos;
    }

    /*
    * findById(Integer id)
    * id - ID of hotel that needs to be found
    *
    * If no hotel exists with that ID, EntityNotFoundException is thrown,
    * else it is returned the hotel object
    * */
    @Override
    public Hotel findById(Integer id) {
        Optional<Hotel> optional = hotelRepository.findById(id);

        if (optional.isEmpty()) throw new EntityNotFoundException("No hotel with this id: " + id);
        logger.trace("Hotel with id {} found", id);
        return optional.get();
    }

    /*
    * existsById(Integer id)
    * id - ID of hotel that needs to be checked
    * If a hotel with this id exits, true is returned, false otherwise.
    * */
    @Override
    public boolean existsById(Integer id) {
        return hotelRepository.existsById(id);
    }

    /*
    * addHotel(HotelCreationRequest request)
    * request - The data and info of hotel that needs to be added
    *
    * If no user exists with the id specified, EntityNotFoundException is thrown
    * If file size is larger than expected ImageTooLargeException is thrown
    * If file type is not image, IncorrectFileTypeException is thrown
    *
    * Saves the file in the specified folder, and then the Hotel data and image path
    * are stored in the repository.
    *
    * Returns true if saved successfully, and false otherwise.
    * */
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

        String filePath = FOLDER_PATH + file.getOriginalFilename();
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            logger.error("Could not upload file: {}", e.getMessage());
            return false;
        }

        Hotel newHotel = Hotel.builder()
                .owner(owner.get())
                .hotelName(request.getHotelName())
                .hotelImagePath(filePath)
                .freeParking(request.getFreeParking().equalsIgnoreCase("true"))
                .freeWiFi(request.getFreeWiFi().equalsIgnoreCase("true"))
                .freePool(request.getFreePool().equalsIgnoreCase("true"))
                .freeBreakfast(request.getFreeBreakfast().equalsIgnoreCase("true"))
                .build();
        logger.info("Hotel added: {} from owner {}", newHotel.getHotelName(), request.getOwnerId());
        hotelRepository.save(newHotel);
        return true;
    }

    /*
    * removeHotel(Integer idOfHotelToBeRemoved)
    * idOfHotelToBeRemoved - ID of the hotels that needs to be removed
    *
    * If no hotel is found with that id, EntityNotFoundException is thrown.
    *
    * If the file path specified does not exist, EntityNotFoundException is thrown
    * If the file in that path cannot be deleted, method returns false.
    *
    * If deletion is successful, true is returned
    * */
    @Override
    public void removeHotel(Integer idOfHotelToBeRemoved) throws IOException {
        if (!hotelRepository.existsById(idOfHotelToBeRemoved)) throw new EntityNotFoundException("No hotel with this id: " + idOfHotelToBeRemoved);

        String pathOfImageToDelete = hotelRepository.findById(idOfHotelToBeRemoved).orElseThrow().getHotelImagePath();
        File file = new File(pathOfImageToDelete);

        if (file.exists() && file.isFile()) {
            try{
                Files.delete(Path.of(file.getAbsolutePath()));
            }
            catch (IOException e){
                logger.error("Could not delete file: {}", e.getMessage());
            }
        } else {
            logger.warn("Could not delete file in path {}, not there", pathOfImageToDelete);
            throw new EntityNotFoundException("File not found: " + pathOfImageToDelete);
        }
        hotelRepository.deleteById(idOfHotelToBeRemoved);
    }
}
