package com.algorhythm.booking_backend.controllers;

import com.algorhythm.booking_backend.dataproviders.Booking.BookingHistoryDto;
import com.algorhythm.booking_backend.services.interfaces.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Tag(
        name = "Bookings",
        description = "All APIs of bookings"
)
public class BookingController {

    public final BookingService bookingService;

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
}
