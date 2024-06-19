package com.algorhythm.booking_backend.dataproviders.Booking;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequest {
    @NotNull(message = "Check in date should not be null")
    private LocalDate checkInDate;
    @NotNull(message = "Check out date should not be null")
    private LocalDate checkOutDate;
    @NotNull(message = "Adults number should not be null, min = 1")
    @Min(1)
    private Integer adultsNumber;
    @NotNull(message = "Kids number should not be null")
    @Min(0)
    private Integer kidsNumber;
}
