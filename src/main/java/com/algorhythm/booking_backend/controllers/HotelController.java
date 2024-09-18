package com.algorhythm.booking_backend.controllers;

import com.algorhythm.booking_backend.dataproviders.dtos.booking.HotelSearchRequest;
import com.algorhythm.booking_backend.dataproviders.dtos.hotel.AvailableHotelDto;
import com.algorhythm.booking_backend.dataproviders.dtos.hotel.HotelCreationRequest;
import com.algorhythm.booking_backend.dataproviders.dtos.hotel.HotelDTO;
import com.algorhythm.booking_backend.dataproviders.services.interfaces.HotelService;
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

    /*
     * getAllHotels()
     *
     * Must be an admin to access this endpoint
     * Returns a list of all hotels in the system
     *
     * If it encounters an error, by default a response with bad_request status is returned
     */
    @GetMapping("/all")
    @Operation(summary = "Get all hotels")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HotelDTO>> getAllHotels(){
        return new ResponseEntity<>(hotelService.allHotelDtos(), HttpStatus.OK);
    }

    /*
     * getAllHotelsByOwner(Integer ownerId)
     * ownerId - ID of the owner to fetch their hotels
     *
     * Must be an admin to access this endpoint
     * Returns a list of hotels owned by the specified owner
     *
     * If it encounters an error, by default a response with bad_request status is returned
     */
    @GetMapping("/allByOwner")
    @Operation(summary = "Get all hotels by owner")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<HotelDTO>> getAllHotelsByOwner(@RequestParam Integer ownerId){
        return ResponseEntity.ok(hotelService.allHotelDtosByOwner(ownerId));
    }

    /*
     * getAllAvailableHotels(HotelSearchRequest request, Integer pageNo)
     * request - Contains the criteria for searching available hotels
     * pageNo - Number of the page that should be returned
     *
     * Must be a user to access this endpoint
     * Returns a page of AvailableHotelDto matching the search criteria
     *
     * If it encounters an error, by default a response with bad_request status is returned
     */
    @PostMapping("/availableHotels")
    @Operation(summary = "Get all available hotels for a certain booking request")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<AvailableHotelDto>> getAllAvailableHotels(@Valid @RequestBody HotelSearchRequest request, @RequestParam Integer pageNo) throws Exception {
        Page<AvailableHotelDto> pageRequested = hotelService.findAllAvailableHotels(request, pageNo);
        return ResponseEntity.ok(pageRequested);
    }

    /*
     * createHotel(HotelCreationRequest request)
     * request - Contains the details for creating a new hotel
     *
     * Must be an admin to access this endpoint
     * Returns a response indicating the success or failure of the hotel creation
     *
     * If the hotel cannot be added, a response with bad_request status is returned
     */
    @PostMapping("/create")
    @Operation(summary = "Create a new hotel")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createHotel(@Valid @ModelAttribute HotelCreationRequest request){
        boolean hotelAdded = hotelService.addHotel(request);
        if (!hotelAdded)
            return ResponseEntity.badRequest().body("Could not save file");
        else return ResponseEntity.ok("Hotel saved successfully");
    }

    /*
     * deleteHotel(Integer id)
     * id - ID of the hotel to be deleted
     *
     * Must be an admin to access this endpoint
     * Returns a response indicating the success or failure of the hotel deletion
     *
     * If the deletion is not successful, a response with bad_request status is returned
     */
    @DeleteMapping("/delete")
    @Operation(summary = "Delete a hotel by its ID")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteHotel(@RequestParam Integer id){
        try{
            hotelService.removeHotel(id);
            return ResponseEntity.ok("Deleted successfully");
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("Deletion not made successfully!");
        }
    }
}
