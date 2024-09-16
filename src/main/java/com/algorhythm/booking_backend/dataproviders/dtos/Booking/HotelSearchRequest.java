package com.algorhythm.booking_backend.dataproviders.dtos.Booking;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelSearchRequest {
    @NotBlank(message = "Check in date should not be null")
    private LocalDate checkInDate;
    @NotBlank(message = "Check out date should not be null")
    private LocalDate checkOutDate;
    @NotBlank(message = "Adults number should not be null, min = 1")
    @Min(1)
    private Integer adultsNumber;
    @NotBlank(message = "Kids number should not be null")
    @Min(0)
    private Integer kidsNumber;
}
