package com.algorhythm.booking_backend.dataproviders.dtos.Booking;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class RoomSearchRequest {
    @NotNull(message = "Hotel id shouldn't be null")
    private Integer hotelId;

    @NotNull(message = "User id shouldn't be null")
    private Integer userId;

    @NotNull(message = "Start date shouldn't be null")
    private LocalDate startDate;

    @NotNull(message = "End date shouldn't be null")
    private LocalDate endDate;

    @NotNull(message = "Adults capacity shouldn't be null")
    @Min(value = 1, message = "Adults capacity should be at least 1")
    private Integer adultsCapacity;

    @NotNull(message = "Kids capacity shouldn't be null")
    @Min(value = 0, message = "Kids capacity should be at least 0")
    private Integer kidsCapacity;

    private String sort;
}
