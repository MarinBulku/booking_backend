package com.algorhythm.booking_backend.adapter.in.models.booking;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Builder
@Data
 public class BookingDto {

     Integer bookingId;

     Integer userId;

     Integer roomId;

     LocalDate startDate;

     LocalDate endDate;

     Double pricePaid;

     String status;

     String reservedFor;

     String email;

     String address;

     String phoneNumber;
}
