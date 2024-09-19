package com.algorhythm.booking_backend.dataproviders.services.implementations;

import com.algorhythm.booking_backend.dataproviders.dtos.booking.BookingRequest;
import com.algorhythm.booking_backend.dataproviders.dtos.booking.RoomSearchRequest;
import com.algorhythm.booking_backend.dataproviders.dtos.room.AvailableRoomDto;
import com.algorhythm.booking_backend.dataproviders.dtos.room.DatePrice;
import com.algorhythm.booking_backend.dataproviders.dtos.room.RoomCreationRequest;
import com.algorhythm.booking_backend.dataproviders.entities.*;
import com.algorhythm.booking_backend.dataproviders.repositories.*;
import com.algorhythm.booking_backend.core.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.core.exceptions.ImageTooLargeException;
import com.algorhythm.booking_backend.core.exceptions.IncorrectFileTypeException;
import com.algorhythm.booking_backend.dataproviders.services.interfaces.RoomService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    /*
    * Room Service method implementations
    * RoomRepository - To perform CRUD operations
    * Other repositories - Mostly to check existence and confirmation
    * */
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final DiscountDateRepository discountDateRepository;
    private final PointRepository pointRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final Logger logger = LoggerFactory.getLogger(RoomServiceImpl.class);

    /*
    * findAll() - No parameters
    * Returns a list of ALL rooms
     * */
    @Override
    public List<Room> findAll() {
        logger.trace("List of all rooms requested");
        return roomRepository.findAll();
    }

    /*
    * findAvailableRoomsToBook2(RoomSearchRequest request, Integer pageNo)
    * request - Specifications of the rooms that need to be found
    * pageNo - Page number that will be returned
    *
    * If request's start date is not before end date, it throws IllegalArgumentException
    * If there isn't any user or hotel by given IDs on the request, EntityNotFoundException is thrown
    *
    * Finds the available rooms by the criteria specified in the request
    * Returns the page of AvailableRoomDto requested
    * */
    @Override
    public Page<AvailableRoomDto> findAvailableRoomsToBook2(RoomSearchRequest request, Integer pageNo) {

        if (request.getStartDate().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Start date cannot be before today");

        if (!request.getStartDate().isBefore(request.getEndDate()))
            throw new IllegalArgumentException("Start date must be before end date");

        Optional<User> userOptional = userRepository.findById(request.getUserId());
        if (userOptional.isEmpty())
            throw new EntityNotFoundException("No user with this id: " + request.getUserId());

        final int pageSize = 18 ;
        Optional<Hotel> optionalHotel = hotelRepository.findById(request.getHotelId());
        if (optionalHotel.isEmpty()) throw new EntityNotFoundException("No hotel with this id: " + request.getHotelId());

        /*
         * we need 18 rooms per page, the results will have number of rooms * dates count records,
         * so the page size of results in database should be 18 * no. of dates
         * */
        int offset = (int) request.getStartDate().datesUntil(request.getEndDate().plusDays(1)).count();
        Pageable pageable = PageRequest.of(pageNo, pageSize * offset);

        /*
        * Based on the request, we get a sorted or not list of object[]
        * */
        List<Object[]> allAvailableRooms = switch (request.getSort()){
          case "+" -> roomRepository.findAvailableRoomsASC(
                  request.getHotelId(),
                  request.getStartDate(),
                  request.getEndDate(),
                  request.getAdultsCapacity(),
                  request.getKidsCapacity(),
                  userOptional.get().getBookingPoints(),
                  pageable
          );
          case "-" -> roomRepository.findAvailableRoomsDESC(
                  request.getHotelId(),
                  request.getStartDate(),
                  request.getEndDate(),
                  request.getAdultsCapacity(),
                  request.getKidsCapacity(),
                  userOptional.get().getBookingPoints(),
                  pageable
          );

            default -> roomRepository.findAvailableRooms2(
                    request.getHotelId(),
                    request.getStartDate(),
                    request.getEndDate(),
                    request.getAdultsCapacity(),
                    request.getKidsCapacity(),
                    userOptional.get().getBookingPoints(),
                    pageable
            );
        };

        if (allAvailableRooms.isEmpty()) {
            logger.trace("No available rooms");
            return new PageImpl<>(new ArrayList<>(), pageable, 0);
        }

        List<AvailableRoomDto> availableRoomsList = new ArrayList<>();
        /*
        * We convert the Object[] to AvailableRoomDto
        * */
        for (Object[] row : allAvailableRooms) {
            Integer roomId = (Integer) row[0];
            String roomName = (String) row[1];
            String description = (String) row[2];
            String roomImageEncoded;
            try {
                roomImageEncoded = Base64.getEncoder().encodeToString(
                        FileUtils.readFileToByteArray(new File((String) row[3]))
                );
            } catch (IOException e) {
                roomImageEncoded = "";
                logger.error("Failed to read roles, {}", e.getMessage());
            }

            Instant bookingDateTime = (Instant) row[4];
            LocalDate bookingDate = bookingDateTime.atZone(ZoneId.systemDefault()).toLocalDate();
            Double dailyCost = (Double) row[5];
            Double totalCost = (Double) row[6];
            Double discountApplied = (Double) row[7];

            AvailableRoomDto roomDto = null;
            for (AvailableRoomDto dto : availableRoomsList) {
                if (dto.getRoomId().equals(roomId)) {
                    roomDto = dto;
                    break;
                }
            }

            if (roomDto == null) {
                roomDto = AvailableRoomDto.builder()
                        .roomId(roomId)
                        .roomName(roomName)
                        .description(description)
                        .roomImage(roomImageEncoded)
                        .totalPrice(totalCost)
                        .datePriceList(new ArrayList<>())
                        .pointsDiscount(discountApplied)
                        .build();
                availableRoomsList.add(roomDto);
            }

            roomDto.getDatePriceList().add(DatePrice.builder()
                    .date(bookingDate)
                    .price(dailyCost)
                    .build());
        }
        /*
        * In the end, we have max 18 rooms per page to return
        * */
        return new PageImpl<>(availableRoomsList, PageRequest.of(pageNo, pageSize), availableRoomsList.size());
    }

    /*
    * getPointDiscountByRoom(Room room, User user)
    * room - room that we get the points discount.
    * user - user that has the points
    *
    * Returns the max discount the user can get with his points
    * */
    private Double getPointDiscountByRoom(Room room, User user) {

        Integer userPoints = user.getBookingPoints();
        List<Points> discountPoints = pointRepository.findByRoom_RoomId(room.getRoomId());
        Double discount = 1.0;
        for (Points p:
             discountPoints) {
            if (userPoints > p.getRequiredPoints())
                discount = p.getDiscount();
        }

        return discount;
    }

    /*
    * getRoomsDatePriceList(Room room, LocalDate startDate, LocalDate endDate)
    * room - room to get the DatePrice list from
    * startDate - The first date of the list
    * endDate - The last date in the list
    *
    * Returns a sorted array list of each date and the price for the room
    * */
    private ArrayList<DatePrice> getRoomsDatePriceList(Room room, LocalDate startDate, LocalDate endDate) {

        if (!roomRepository.existsById(room.getRoomId()))
            throw new EntityNotFoundException("No room found with this id: " + room.getRoomId());

        List<DiscountDate> discountDates = discountDateRepository.findByDiscountDateBetweenAndRoom_RoomId(startDate, endDate, room.getRoomId());

        Map<LocalDate, Double> discountMap = discountDates.stream()
                .collect(Collectors.toMap(DiscountDate::getDiscountDate, DiscountDate::getDiscount));

        List<LocalDate> allDates = startDate.datesUntil(endDate.plusDays(1)).toList();
        ArrayList<DatePrice> datePrices = new ArrayList<>(allDates.size());

        for (LocalDate date : allDates) {
            Double discount = discountMap.get(date);
            double price;

            if (discount != null) {
                price = room.getPrice() * discount;
            } else {
                price = switch (date.getDayOfWeek()) {
                    case MONDAY, TUESDAY -> room.getPrice() * 0.9;
                    case SATURDAY, SUNDAY -> room.getPrice() * 1.1;
                    default -> room.getPrice();
                };
            }

            datePrices.add(
                    DatePrice.builder()
                            .date(date)
                            .price(price)
                            .build()
            );
        }

        return datePrices;
    }

    /*
    * findById(Integer id)
    * id - ID of the room to be found
    * If no room is found, EntityNotFoundException is thrown
    * Else the room is returned
    * */
    @Override
    public Room findById(Integer id) {

        Optional<Room> optional = roomRepository.findById(id);
        if (optional.isEmpty()) throw new EntityNotFoundException("No room with this id: " + id);

        logger.trace("Room with id {} found", id);
        return optional.get();
    }

    /*
    * existsById(Integer id)
    * id - ID of the room to be checked
    *
    * Returns true if a room exists by given id, otherwise false is returned
    * */
    @Override
    public boolean existsById(Integer id) {
        return roomRepository.existsById(id);
    }

    /*
    * addRoom(RoomCreationRequest request)
    * request - specifications of the room to be created
    *
    * If no hotel is found by the id in the request, EntityNotFoundException is thrown
    * If file size is large, ImageTooLargeException is thrown
    * If file sent is not image, IncorrectFileTypeException is thrown
    *
    * Saves the room image in the roomImages folder, and stores the path in the room entity.
    * If the image cannot be saved, false is returned.
    *
    * Room entity is created and saved, method returns true
    * */
    @Override
    public boolean addRoom(RoomCreationRequest request) {

        Optional<Hotel> optional = hotelRepository.findById(request.getHotelId());
        if (optional.isEmpty()) throw new EntityNotFoundException("No hotel found with this id: " + request.getHotelId());

        if (!userRepository.existsById(request.getOwnerId())) {
            throw new EntityNotFoundException("No hotel owner with id: " + request.getOwnerId());
        }

        if (!Objects.equals(optional.get().getOwner().getUserId(), request.getOwnerId())) {
            throw new EntityNotFoundException("This user is not the owner of the hotel, id: " + request.getOwnerId());
        }

        MultipartFile file = request.getRoomImage();
        if (file.getSize() > 102400)
            throw new ImageTooLargeException("Image size larger than 100KB: " + file.getSize());
        else if (!Objects.requireNonNull(file.getContentType()).startsWith("image/")) {
            throw new IncorrectFileTypeException("File type provided not image: " +file.getContentType());
        }

        String folderPath = "C:\\Users\\ALGORHYTHM\\Documents\\Booking\\booking_backend\\src\\main\\resources\\roomImages\\";
        String filePath = folderPath + file.getOriginalFilename();
        try {
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            logger.error("Failed to save room, {}", e.getMessage());
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
        logger.info("New room created");
        return true;
    }

    /*
    * removeRoom(Integer idOfRoomToBeRemoved)
    * idOfRoomToBeRemoved - ID of the room to be removed
    *
    * If room does not exist, or file of room cannot be found, EntityNotFoundException is thrown
    * Else, room is deleted and method returns true
    * */
    @Override
    public boolean removeRoom(Integer idOfRoomToBeRemoved) throws IOException {
        if (!roomRepository.existsById(idOfRoomToBeRemoved)) throw new EntityNotFoundException("No room with this id: " + idOfRoomToBeRemoved);
        String pathOfImageToDelete = roomRepository.findById(idOfRoomToBeRemoved).orElseThrow(() -> new EntityNotFoundException("No room with this id:" + idOfRoomToBeRemoved)).getRoomImagePath();
        File file = new File(pathOfImageToDelete);

        if (file.exists() && file.isFile()) {
            cleanupFile(file);
        } else {
            throw new IOException("File not found: " + pathOfImageToDelete);
        }
        roomRepository.deleteById(idOfRoomToBeRemoved);
        return true;
    }

    private void cleanupFile(File file) {
        try {
            Files.delete(Path.of(file.getAbsolutePath()));
        }catch (IOException e) {
            logger.error("Failed to delete photo, {}", e.getMessage());
        }
    }

    /*
    * bookRoom(BookingRequest request)
    * request - Booking specifications
    *
    * If the user that books or room to be booked does not exist, EntityNotFoundException is thrown
    *
    * Credit card and user details are checked, if not correct method returns false
    * If the room is booked between the specified dates in the request, false is returned
    *
    * We check if the price details are correct, if not false is returned
    *
    * Otherwise, a booking is saved in the repository and true is returned
    * */
    @Override
    public boolean bookRoom(BookingRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new EntityNotFoundException("No user with this id:" + request.getUserId()));
        Room room = roomRepository.findById(request.getRoomId()).orElseThrow(() -> new EntityNotFoundException("No room with this id:" + request.getRoomId()));

        if (request.getStartDate().isBefore(LocalDate.now()))
            throw new IllegalArgumentException("Start date cannot be before today");

        if (!request.getStartDate().isBefore(request.getEndDate()))
            throw new IllegalArgumentException("Start date must be before end date");


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        YearMonth yearMonth = YearMonth.parse(request.getCCExpiryDate(), formatter);
        LocalDate ccExpiryDate = yearMonth.atDay(1);


        if (
                request.getReservedFor().isBlank()
                || request.getEmail().isBlank()
                        || request.getAddress().isBlank()
                        || request.getPhoneNumber().isBlank()
        ) {
            logger.warn("Person reserved for information is invalid");
            return false;
        }

        if (request.getCCName().isBlank()
                || request.getCCNumber().length() != 16
                || request.getCVV().length() != 3
                || ccExpiryDate.isBefore(LocalDate.now())
        ) {
            logger.warn("Credit card info is invalid");
            return false;
        }

        Optional<Booking> isThereABooking = bookingRepository.isThereABooking(
                room,
                request.getStartDate(),
                request.getEndDate()
        );

        if (isThereABooking.isPresent())
            throw new EntityExistsException("Room already booked between those dates!");

        Double bookingPrice = getRoomsDatePriceList(room, request.getStartDate(), request.getEndDate()).stream().mapToDouble(DatePrice::getPrice).sum();
        Double discountPoints = getPointDiscountByRoom(room, user);

        Double roundedPrice = BigDecimal.valueOf(bookingPrice * discountPoints)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        if (!roundedPrice.equals(request.getPrice())) {
            logger.error("Room price from request is not what it should be : {} != {}", request.getPrice(), roundedPrice);
            return false;
        }

        Booking newBooking = Booking.builder()
                .user(user)
                .room(room)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .pricePaid(roundedPrice)
                .status(Status.ACTIVE)
                .reservedFor(request.getReservedFor())
                .email(request.getEmail())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .build();

        bookingRepository.save(newBooking);
        logger.trace("New booking created");
        user.setBookingsNumber(user.getBookingsNumber()+1);
        user.setBookingPoints(user.getBookingPoints()+2);

        userRepository.save(user);

        return true;
    }
}
