package com.algorhythm.booking_backend.controllers;

import com.algorhythm.booking_backend.dataproviders.Booking.BookingHistoryDto;
import com.algorhythm.booking_backend.services.interfaces.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Tag(
        name = "Bookings",
        description = "All APIs of bookings"
)
public class BookingController {

    public final BookingService bookingService;

    /*
    * getUsersBookingHistory(Integer userId, Integer pageSize, Integer pageNo, String sorted)
    * userId - ID of user to search for his bookings
    * pageSize - Number of bookings that should be displayed for this page
    * pageNo - Number of page that should be returned
    * sorted - if the contents should be returned
    *
    * Must be a user to access this endpoint
    * It returns a page of BookingHistoryDto of the user specified
    *
    * If it encounters an error, by default a response with bad_request status is returned
    * */
    @GetMapping("/history")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get booking history of user")
    public ResponseEntity<Page<BookingHistoryDto>> getUsersBookingHistory(
            @RequestParam Integer userId,
            @RequestParam Integer pageSize,
            @RequestParam Integer pageNo,
            @RequestParam String sorted
    ){
        return ResponseEntity.ok(bookingService.findAllBookingsFromUser(userId, pageSize, pageNo, sorted));
    }

    /*
    * cancelBookReservation(Integer bookingId, Integer userId)
    * bookingId - ID of the booking to be cancelled
    * userID - The user that made the booking
    *
    * Must be a user to access this endpoint
    * If cancelled successfully, response with 200 status is returned,
    * else user with bad_request status is returned
    * */
    @PutMapping("/cancel")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Cancel a booking")
    public ResponseEntity<String> cancelBookReservation(@RequestParam Integer bookingId,
                                                      @RequestParam Integer userId){
        boolean cancelled = bookingService.cancelBooking(bookingId, userId);

        if (cancelled)
            return ResponseEntity.ok("Cancelled successfully");

        return new ResponseEntity<>("User doesn't match with the booking user", HttpStatus.BAD_REQUEST);
    }
}
