package com.algorhythm.booking_backend.services.implementations;

import com.algorhythm.booking_backend.entities.Booking;
import com.algorhythm.booking_backend.entities.Room;
import com.algorhythm.booking_backend.entities.User;
import com.algorhythm.booking_backend.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.repositories.BookingRepository;
import com.algorhythm.booking_backend.repositories.RoomRepository;
import com.algorhythm.booking_backend.repositories.UserRepository;
import com.algorhythm.booking_backend.services.interfaces.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<Booking> findAll() {

        List<Booking> allBookings = bookingRepository.findAll();

        if (allBookings.isEmpty()) return new ArrayList<>();

        return allBookings;
    }

    @Override
    public List<Booking> findAllBookingsFromUser(Integer userId) {

        List<Booking> allBookingsFromUser = bookingRepository.findByUserId(userId);

        if (allBookingsFromUser.isEmpty()) return new ArrayList<>();

        return allBookingsFromUser;
    }

    @Override
    public Booking findById(Integer bookingId) {

        Optional<Booking> optional = bookingRepository.findById(bookingId);

        if (optional.isEmpty()) throw new EntityNotFoundException("Booking not found!");

        return optional.get();
    }

    @Override
    public void cancelBooking(Integer bookingId) {
        Optional<Booking> optional = bookingRepository.findById(bookingId);

        if (optional.isEmpty()) throw new EntityNotFoundException("Booking not found!");

        Booking bookingToBeCanceled = optional.get();
        bookingToBeCanceled.setCancelled(true);
        bookingRepository.save(bookingToBeCanceled);
    }

    @Override
    public Booking bookRoom(Integer roomId, LocalDate startDate, LocalDate endDate, Integer userId, Double price) {

        if (!userRepository.existsById(userId)) throw new EntityNotFoundException("User with this id not found: " + userId);
        else if (!roomRepository.existsById(roomId)) throw new EntityNotFoundException("Room with this id not found: " + roomId);

        User user = userRepository.findById(userId).get();
        Room room = roomRepository.findById(roomId).get();

        Booking newBooking = Booking.builder()
                .user(user)
                .room(room)
                .startDate(startDate)
                .endDate(endDate)
                .pricePaid(price)
                .isCancelled(false)
                .build();

        return bookingRepository.save(newBooking);
    }
}
