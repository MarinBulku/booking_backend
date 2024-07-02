package com.algorhythm.booking_backend.services.implementations;

import com.algorhythm.booking_backend.dataproviders.Booking.BookingHistoryDto;
import com.algorhythm.booking_backend.entities.Booking;
import com.algorhythm.booking_backend.entities.Room;
import com.algorhythm.booking_backend.entities.Status;
import com.algorhythm.booking_backend.entities.User;
import com.algorhythm.booking_backend.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.repositories.BookingRepository;
import com.algorhythm.booking_backend.repositories.RoomRepository;
import com.algorhythm.booking_backend.repositories.UserRepository;
import com.algorhythm.booking_backend.services.interfaces.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Override
    public List<Booking> findAll() {

        List<Booking> allBookings = bookingRepository.findAll();

        if (allBookings.isEmpty()) return new ArrayList<>();

        return allBookings;
    }

    @Override
    public Page<BookingHistoryDto> findAllBookingsFromUser(Integer userId, Integer pageSize, Integer pageNo, String orderBy) {

        if (pageSize <= 0 || pageNo <0)
            throw new IllegalArgumentException("Invalid page arguments");

        Sort sort = switch (orderBy){
            case "+" -> Sort.by("startDate").ascending();
            case "-" -> Sort.by("startDate").descending();
            default -> Sort.unsorted();
        };
        Pageable page = PageRequest.of(pageNo,pageSize, sort);
        return bookingRepository.findByUser_UserId(userId, page);
    }

    @Override
    public Booking findById(Integer bookingId) {

        Optional<Booking> optional = bookingRepository.findById(bookingId);

        if (optional.isEmpty()) throw new EntityNotFoundException("Booking not found!");

        return optional.get();
    }

    @Override
    public boolean cancelBooking(Integer bookingId, Integer userId) {
        Optional<Booking> optional = bookingRepository.findById(bookingId);

        if (optional.isEmpty()) throw new EntityNotFoundException("Booking not found!");

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) throw new EntityNotFoundException("User not found!");

        Booking bookingToBeCanceled = optional.get();

        if (bookingToBeCanceled.getUser() != optionalUser.get())
            return false;

        bookingToBeCanceled.setStatus(Status.CANCELLED);
        bookingRepository.save(bookingToBeCanceled);

        return true;
    }

    @Override
    public Booking bookRoom(Integer roomId, LocalDate startDate, LocalDate endDate, Integer userId, Double price) {

        return null;
    }
}
