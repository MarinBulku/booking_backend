package com.algorhythm.booking_backend.dataproviders.Booking;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
@Getter
@AllArgsConstructor
public class BookingRequest {
    @NotNull
    private Integer roomId;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;
    @NotNull
    private Integer userId;
    @NotNull
    private Double price;

    @NotNull
    private String reservedFor;
    @NotNull
    private String email;
    @NotNull
    private String address;
    @NotNull
    private String phoneNumber;

    @NotNull
    private String CCName;
    @NotNull
    private String CCNumber;
    @NotNull
    private String CVV;
    @NotNull
    private String CCExpiryDate;
}
