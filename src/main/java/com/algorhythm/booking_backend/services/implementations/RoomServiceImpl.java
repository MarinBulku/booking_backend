package com.algorhythm.booking_backend.services.implementations;

import com.algorhythm.booking_backend.dataproviders.Booking.BookingRequest;
import com.algorhythm.booking_backend.dataproviders.Booking.RoomSearchRequest;
import com.algorhythm.booking_backend.dataproviders.Room.AvailableRoomDto;
import com.algorhythm.booking_backend.dataproviders.Room.DatePrice;
import com.algorhythm.booking_backend.dataproviders.Room.RoomCreationRequest;
import com.algorhythm.booking_backend.entities.*;
import com.algorhythm.booking_backend.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.exceptions.ImageTooLargeException;
import com.algorhythm.booking_backend.exceptions.IncorrectFileTypeException;
import com.algorhythm.booking_backend.repositories.*;
import com.algorhythm.booking_backend.services.interfaces.RoomService;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final DiscountDateRepository discountDateRepository;
    private final PointRepository pointRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    @Override
    public List<Room> findAll() {
        List<Room> allRooms = roomRepository.findAll();
        if (allRooms.isEmpty()) return new ArrayList<>();
        return allRooms;
    }


    @Override
    public Page<AvailableRoomDto> findAvailableRoomsToBook2(RoomSearchRequest request, Integer pageNo) {

        if (!request.getStartDate().isBefore(request.getEndDate()))
            throw new IllegalArgumentException("Start date must be before end date");

        Optional<User> userOptional = userRepository.findById(request.getUserId());
        if (userOptional.isEmpty())
            throw new EntityNotFoundException("No user with this id: " + request.getUserId());

        final int pageSize = 18 ;
        /*
        * we need 18 rooms per page, the results will have number of rooms * dates count records,
        * so the page size of results in database should be 18 * no. of dates
        * */
        int offset = (int) request.getStartDate().datesUntil(request.getEndDate().plusDays(1)).count();
        Pageable pageable = PageRequest.of(pageNo, pageSize * offset);
        Optional<Hotel> optionalHotel = hotelRepository.findById(request.getHotelId());

        if (optionalHotel.isEmpty()) throw new EntityNotFoundException("No hotel with this id: " + request.getHotelId());

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

        if (allAvailableRooms.size() == 0)
            return new PageImpl<>(new ArrayList<>(), pageable, 0);

        List<AvailableRoomDto> availableRoomsList = new ArrayList<>();

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
        //System.out.println();
        return new PageImpl<>(availableRoomsList, PageRequest.of(pageNo, pageSize), availableRoomsList.size());
    }

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

    @Override
    public boolean bookRoom(BookingRequest request) {
        if (!userRepository.existsById(request.getUserId())) throw new EntityNotFoundException("User with this id not found: " + request.getUserId());
        else if (!roomRepository.existsById(request.getRoomId())) throw new EntityNotFoundException("Room with this id not found: " + request.getRoomId());

        if (
                request.getReservedFor().isBlank()
                || request.getEmail().isBlank()
                        || request.getAddress().isBlank()
                        || request.getPhoneNumber().isBlank()
        ) return false;

        if (request.getCCName().isBlank()
                || request.getCCNumber().isBlank()
                || request.getCCNumber().length() != 16
                || request.getCVV().isBlank()
                || request.getCVV().length() != 3
                || request.getCCExpiryDate().isBefore(LocalDate.now())
        ) return false;

        Room room = roomRepository.findById(request.getRoomId()).get();

        Optional<Booking> isThereABooking = bookingRepository.isThereABooking(
                room,
                request.getStartDate(),
                request.getEndDate()
        );

        if (isThereABooking.isPresent())
            throw new EntityExistsException("Room already booked between those dates!");

        Double bookingPrice = getRoomsDatePriceList(room, request.getStartDate(), request.getEndDate()).stream().mapToDouble(DatePrice::getPrice).sum();
        User user = userRepository.findById(request.getUserId()).get();
        Double discountPoints = getPointDiscountByRoom(room, user);
        Double priceThatShouldBePaid = bookingPrice * discountPoints;

        if (!priceThatShouldBePaid.equals(request.getPrice()))
            return false;

        Booking newBooking = Booking.builder()
                .user(user)
                .room(room)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .pricePaid(request.getPrice())
                .status(Status.ACTIVE)
                .reservedFor(request.getReservedFor())
                .email(request.getEmail())
                .address(request.getAddress())
                .phoneNumber(request.getPhoneNumber())
                .build();

        bookingRepository.save(newBooking);

        user.setBookingsNumber(user.getBookingsNumber()+1);
        user.setBookingPoints(user.getBookingPoints()+2);

        userRepository.save(user);

        return true;
    }
}
