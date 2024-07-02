package com.algorhythm.booking_backend.controllers;

import com.algorhythm.booking_backend.dataproviders.Booking.BookingRequest;
import com.algorhythm.booking_backend.dataproviders.Booking.RoomSearchRequest;
import com.algorhythm.booking_backend.dataproviders.Room.AvailableRoomDto;
import com.algorhythm.booking_backend.dataproviders.Room.RoomCreationRequest;
import com.algorhythm.booking_backend.services.interfaces.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@Tag(
        name = "Rooms",
        description = "All APIs of Rooms"
)
public class RoomController {

    public final RoomService roomService;

    @PostMapping("/create")
    @Operation(summary = "Add a new room")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createRoom(@Valid @ModelAttribute RoomCreationRequest request){
        boolean addedRoom = roomService.addRoom(request);
        if (!addedRoom)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not save file");
        else return ResponseEntity.ok("Room saved successfully");
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete a hotel by its ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteRoom(@RequestParam Integer id){
        if (roomService.removeRoom(id)) {
            return ResponseEntity.ok("Deleted successfully");
        }
        return ResponseEntity.badRequest().body("Deletion not made successfully!");
    }

    @PostMapping("/available")
    @Operation(summary = "Get all available rooms for a certain room search request")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<AvailableRoomDto>> getAvailableRooms(@Valid @RequestBody RoomSearchRequest request, @RequestParam Integer pageNo){
        Page<AvailableRoomDto> rooms = roomService.findAvailableRoomsToBook2(request, pageNo);
        return ResponseEntity.ok(rooms);
    }

    @PostMapping("/book")
    @Operation(summary = "Book a new room")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> bookRoom(@Valid @RequestBody BookingRequest request){
        boolean isBookingDone = roomService.bookRoom(request);

        if (isBookingDone)
            return ResponseEntity.ok("OK");
        else return new ResponseEntity<>("Info not correct, credit card or price sent may be invalid", HttpStatus.BAD_REQUEST);
    }
}
