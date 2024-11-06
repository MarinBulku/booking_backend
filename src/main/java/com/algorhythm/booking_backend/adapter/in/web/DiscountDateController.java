package com.algorhythm.booking_backend.adapter.in.web;

import com.algorhythm.booking_backend.adapter.in.models.discountdate.DiscountDateDto;
import com.algorhythm.booking_backend.application.service.interfaces.DiscountDateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/discount-date")
@Tag(
        name = "Discount Dates",
        description = "All apis of DiscountDate"
)
public class DiscountDateController {

    private final DiscountDateService discountDateService;

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Add a new discount date for a room")
    public ResponseEntity<DiscountDateDto> newDiscountDate(@RequestParam Integer roomId, @RequestParam LocalDate date, @RequestParam Double discount){
        return ResponseEntity.ok(discountDateService.addDiscountDate(roomId, date, discount));
    }

    @GetMapping("/room")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all discount dates of a room")
    public ResponseEntity<List<DiscountDateDto>> getDiscountDatesOfRoom(@RequestParam Integer roomId){
        return ResponseEntity.ok(discountDateService.getRoomDiscountDates(roomId));
    }

    @DeleteMapping("/room")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Remove the discount date of a room")
    public ResponseEntity<String> removeDiscountDate(@RequestParam Integer discountDateId){
        discountDateService.removeDiscountDate(discountDateId);
        return ResponseEntity.ok("Deleted successfully");
    }
}
