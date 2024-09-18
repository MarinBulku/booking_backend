package com.algorhythm.booking_backend.dataproviders.dtos.booking;

import com.algorhythm.booking_backend.dataproviders.entities.Status;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingHistoryDto {
    private Integer bookingId;
    private String hotelName;
    private Double bookingPrice;
    private LocalDate startDate;
    private Status status;

    public BookingHistoryDto(Integer bookingId, String hotelName, Double bookingPrice, LocalDate startDate, Status status) {
        this.bookingId = bookingId;
        this.hotelName = hotelName;
        this.bookingPrice = bookingPrice;
        this.startDate = startDate;
        this.status = status;
    }
}
