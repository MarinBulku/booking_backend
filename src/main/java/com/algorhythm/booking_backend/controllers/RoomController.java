package com.algorhythm.booking_backend.controllers;

import com.algorhythm.booking_backend.dataproviders.dtos.booking.BookingRequest;
import com.algorhythm.booking_backend.dataproviders.dtos.booking.RoomSearchRequest;
import com.algorhythm.booking_backend.dataproviders.dtos.room.AvailableRoomDto;
import com.algorhythm.booking_backend.dataproviders.dtos.room.RoomCreationRequest;
import com.algorhythm.booking_backend.dataproviders.services.interfaces.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/rooms")
@RequiredArgsConstructor
@Tag(
        name = "Rooms",
        description = "All APIs of Rooms"
)
public class RoomController {

    public final RoomService roomService;

    /*
     * createRoom(RoomCreationRequest request)
     * request - Contains the details for creating a new room
     *
     * Must be an admin to access this endpoint
     * Returns a response indicating the success or failure of the room creation
     *
     * If the room cannot be added, a response with bad_request status is returned
     */
    @PostMapping("/create")
    @Operation(summary = "Add a new room")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createRoom(@Valid @ModelAttribute RoomCreationRequest request){
        boolean addedRoom = roomService.addRoom(request);
        if (!addedRoom)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not save file");
        else return ResponseEntity.ok("Room saved successfully");
    }

    /*
     * deleteRoom(Integer id)
     * id - ID of the room to be deleted
     *
     * Must be an admin to access this endpoint
     * Returns a response indicating the success or failure of the room deletion
     *
     * If the deletion is not successful, a response with bad_request status is returned
     */
    @DeleteMapping("/delete")
    @Operation(summary = "Delete a hotel by its ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteRoom(@RequestParam Integer id) {
        try {
            roomService.removeRoom(id);
            return ResponseEntity.ok("Deleted successfully");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Deletion not made successfully! " + e.getMessage());
        }

    }

    /*
     * getAvailableRooms(RoomSearchRequest request, Integer pageNo)
     * request - Contains the criteria for searching available rooms
     * pageNo - Number of the page that should be returned
     *
     * Must be a user to access this endpoint
     * Returns a page of AvailableRoomDto matching the search criteria
     *
     * If it encounters an error, by default a response with bad_request status is returned
     */
    @PostMapping("/available")
    @Operation(summary = "Get all available rooms for a certain room search request")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<AvailableRoomDto>> getAvailableRooms(@Valid @RequestBody RoomSearchRequest request, @RequestParam Integer pageNo){
        Page<AvailableRoomDto> rooms = roomService.findAvailableRoomsToBook2(request, pageNo);
        return ResponseEntity.ok(rooms);
    }

    /*
     * bookRoom(BookingRequest request)
     * request - Contains the details for booking a room
     *
     * Must be a user to access this endpoint
     * Returns a response indicating the success or failure of the room booking
     *
     * If the booking information is incorrect, a response with bad_request status is returned
     */
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
