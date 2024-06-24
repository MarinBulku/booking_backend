package com.algorhythm.booking_backend.controllers;

import com.algorhythm.booking_backend.dataproviders.Booking.BookingRequest;
import com.algorhythm.booking_backend.dataproviders.Hotel.AvailableHotelDto;
import com.algorhythm.booking_backend.dataproviders.Hotel.HotelCreationRequest;
import com.algorhythm.booking_backend.dataproviders.Hotel.HotelDTO;
import com.algorhythm.booking_backend.services.interfaces.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
@Tag(
        name = "Hotels",
        description = "All APIs of hotels"
)
public class HotelController {

    public final HotelService hotelService;

    @GetMapping("/all")
    @Operation(summary = "Get all hotels")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HotelDTO>> getAllHotels(){
        return new ResponseEntity<>(hotelService.allHotelDtos(), HttpStatus.OK);
    }

    @GetMapping("/allByOwner")
    @Operation(summary = "Get all hotels by owner")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HotelDTO>> getAllHotelsByOwner(@RequestParam Integer ownerId){
        return ResponseEntity.ok(hotelService.allHotelDtosByOwner(ownerId));
    }

    @PostMapping("/availableHotels")
    @Operation(summary = "Get all available hotels for a certain booking request")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<AvailableHotelDto>> getAllAvailableHotels(@Valid @RequestBody BookingRequest request, @RequestParam Integer pageNo) throws Exception {
        Page<AvailableHotelDto> pageRequested = hotelService.findAllAvailableHotels(request, pageNo);
        return ResponseEntity.ok(pageRequested);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new hotel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createHotel(@Valid @ModelAttribute HotelCreationRequest request){
        boolean hotelAdded = hotelService.addHotel(request);
        if (!hotelAdded)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not save file");
        else return ResponseEntity.ok("Hotel saved successfully");
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Delete a hotel by its ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteHotel(@RequestParam Integer id){
        if (hotelService.removeHotel(id)) {
            return ResponseEntity.ok("Deleted successfully");
        }
        return ResponseEntity.badRequest().body("Deletion not made successfully!");
    }
}
