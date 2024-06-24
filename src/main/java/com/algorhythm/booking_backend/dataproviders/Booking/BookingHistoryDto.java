package com.algorhythm.booking_backend.dataproviders.Booking;

import com.algorhythm.booking_backend.entities.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingHistoryDto {
    private String hotelName;
    private Double bookingPrice;
    private LocalDate startDate;
    private Status status;

    public BookingHistoryDto(String hotelName, Double bookingPrice, LocalDate startDate, Status status) {
        this.hotelName = hotelName;
        this.bookingPrice = bookingPrice;
        this.startDate = startDate;
        this.status = status;
    }
}
