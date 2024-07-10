package com.algorhythm.booking_backend.services.implementations;

import com.algorhythm.booking_backend.dataproviders.Booking.BookingHistoryDto;
import com.algorhythm.booking_backend.entities.Booking;
import com.algorhythm.booking_backend.entities.Status;
import com.algorhythm.booking_backend.entities.User;
import com.algorhythm.booking_backend.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.repositories.BookingRepository;
import com.algorhythm.booking_backend.repositories.UserRepository;
import com.algorhythm.booking_backend.services.interfaces.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    //Booking Service method implementations

    //BookingRepository - get booking info
    //UserRepository - get user info, mostly confirmation
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    /*
    * findAll() - No parameters needed
    *
    * Returns a list of ALL bookings stored in repository/database.
    * If there aren't any, it returns an empty list.
    * */
    @Override
    public List<Booking> findAll() {

        List<Booking> allBookings = bookingRepository.findAll();

        if (allBookings.isEmpty()) return new ArrayList<>();

        return allBookings;
    }

    /*
    * findAllBookingsFromUser(Integer userId, Integer pageSize, Integer pageNo, String orderBy)
    * userId - The user we get the booking history of.
    * pageSize - The page size(max number of records) that need to be sent
    * pageNo - The page number that needs to be sent
    * orderBy - If needed to, we can sort the page.
    *
    * Finds all bookings made from a user, returns them as a page.
    * */
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

    /*
    * findById(Integer bookingId)
    * bookingId - ID of booking we want to get.
    *
    * Returns a Booking object or null if not present.
    * */
    @Override
    public Booking findById(Integer bookingId) {

        Optional<Booking> optional = bookingRepository.findById(bookingId);

        if (optional.isEmpty()) throw new EntityNotFoundException("Booking not found!");

        return optional.get();
    }

    /*
    * cancelBooking(Integer bookingId, Integer userId)
    * bookingId - ID of booking we have to cancel
    * userId - ID of the user that wants to cancel the booking
    *
    * If both present, we check if the userId matches the userId
    * of the user that made the booking. If equal, booking is cancelled
    * and return true to confirm. False otherwise
    * */
    @Override
    public boolean cancelBooking(Integer bookingId, Integer userId) {
        Optional<Booking> optional = bookingRepository.findById(bookingId);

        if (optional.isEmpty()) throw new EntityNotFoundException("Booking not found!");

        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) throw new EntityNotFoundException("User not found!");

        Booking bookingToBeCanceled = optional.get();

        if (bookingToBeCanceled.getUser() != optionalUser.get())
            return false;

        if (!bookingToBeCanceled.getStatus().equals(Status.ACTIVE))
            return false;

        bookingToBeCanceled.setStatus(Status.CANCELLED);
        bookingRepository.save(bookingToBeCanceled);

        return true;
    }

}
