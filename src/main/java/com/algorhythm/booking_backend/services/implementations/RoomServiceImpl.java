package com.algorhythm.booking_backend.services.implementations;

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
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
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
    @Override
    public List<Room> findAll() {
        List<Room> allRooms = roomRepository.findAll();
        if (allRooms.isEmpty()) return new ArrayList<>();
        return allRooms;
    }

    @Override
    public Page<AvailableRoomDto> findAvailableRoomsToBook(RoomSearchRequest request, Integer pageNo) {
        if (!request.getStartDate().isBefore(request.getEndDate()))
            throw new IllegalArgumentException("Start date must be before end date");

        Optional<User> userOptional = userRepository.findById(request.getUserId());
        if (userOptional.isEmpty())
            throw new EntityNotFoundException("No user with this id: " + request.getUserId());

        final int pageSize = 18;

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Optional<Hotel> optionalHotel = hotelRepository.findById(request.getHotelId());

        if (optionalHotel.isEmpty()) throw new EntityNotFoundException("No hotel with this id: " + request.getHotelId());

        Page<Room> foundRooms = roomRepository.findAvailableRooms(
                optionalHotel.get(),
                request.getStartDate(),
                request.getEndDate(),
                request.getAdultsCapacity(),
                request.getKidsCapacity(),
                pageable
                );

        List<AvailableRoomDto> availableRoomDto = foundRooms.stream().map(room -> {
            try {
                String roomImage = Base64.getEncoder().encodeToString(
                        FileUtils.readFileToByteArray(new File(room.getRoomImagePath()))
                );
                ArrayList<DatePrice> datePrices = getRoomsDatePriceList(room, request.getStartDate(), request.getEndDate());
                Double totalPrice = datePrices.stream().mapToDouble(DatePrice::getPrice).sum();
                Double pointDiscount = getPointDiscountByRoom(room, userOptional.get());
                return AvailableRoomDto.builder()
                        .roomId(room.getRoomId())
                        .roomName(room.getRoomName())
                        .description(room.getDescription())
                        .roomImage(roomImage)
                        .datePriceList(datePrices)
                        .totalPrice(totalPrice * pointDiscount)
                        .pointsDiscount(pointDiscount)
                        .build();
            } catch (IOException e) {
                throw new RuntimeException("Failed to read room image file");
            }
        }).toList();

        ArrayList<AvailableRoomDto> list = new ArrayList<>(availableRoomDto);

        if ("+".equals(request.getSort())) {
            list.sort(Comparator.comparingDouble(AvailableRoomDto::getTotalPrice));
        } else if ("-".equals(request.getSort())) {
            list.sort(Comparator.comparingDouble(AvailableRoomDto::getTotalPrice).reversed());
        }

        return new PageImpl<>(list, pageable, list.size());
    }

    private Double getPointDiscountByRoom(Room room, User user) {

        Integer userPoints = user.getBookingPoints();
        List<Points> discountPoints = pointRepository.findByRoomId(room.getRoomId());
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
}
