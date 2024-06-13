package com.algorhythm.booking_backend.controllers;

import com.algorhythm.booking_backend.dataproviders.Room.RoomCreationRequest;
import com.algorhythm.booking_backend.services.interfaces.RoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<?> createRoom(@ModelAttribute RoomCreationRequest request){
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


}
