package com.algorhythm.booking_backend.dataproviders.services.implementations;

import com.algorhythm.booking_backend.dataproviders.dtos.booking.BookingHistoryDto;
import com.algorhythm.booking_backend.core.entities.Booking;
import com.algorhythm.booking_backend.core.entities.Status;
import com.algorhythm.booking_backend.core.entities.User;
import com.algorhythm.booking_backend.core.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.dataproviders.repositories.BookingRepository;
import com.algorhythm.booking_backend.dataproviders.repositories.UserRepository;
import com.algorhythm.booking_backend.dataproviders.services.interfaces.BookingService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    private final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    /*
    * findAll() - No parameters needed
    *
    * Returns a list of ALL bookings stored in repository/database.
    * If there aren't any, it returns an empty list.
    * */
    @Override
    public List<Booking> findAll() {
        logger.trace("List of all bookings generated");
        return bookingRepository.findAll();
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
    public List<BookingHistoryDto> findAllBookingsFromUser(Integer userId) {
        if (!userRepository.existsById(userId))
            throw new EntityNotFoundException("User not found with ID: " + userId);

        logger.trace("User with id {} requested his booking history", userId);
        return bookingRepository.findByUser_UserId(userId);
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
        logger.trace("Booking with id {} found", bookingId);
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
        Booking optional = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new EntityNotFoundException("Booking with id: " + bookingId + " is not found."));
        User optionalUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User with id: " + userId + " is not found."));

        if (optional.getUser() != optionalUser) {
            logger.warn("User {} with id {} wants to cancel a booking with id {} of {} with id {}",
                    optionalUser.getFullName(),
                    optionalUser.getUserId(),
                    optional.getBookingId(),
                    optional.getUser().getFullName(),
                    optional.getUser().getUserId());
            return false;
        }

        if (!optional.getStatus().equals(Status.ACTIVE)) {
            logger.warn("User with id {} wants to cancel booking {} which is {}",
                    userId,
                    bookingId,
                    optional.getStatus());
            return false;
        }

        optional.setStatus(Status.CANCELLED);
        bookingRepository.save(optional);
        logger.trace("Booking with id {} cancelled", bookingId);
        return true;
    }

}
