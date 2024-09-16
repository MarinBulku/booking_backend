package com.algorhythm.booking_backend.dataproviders.dtos.Room;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DatePrice {
    private LocalDate date;
    private Double price;
}
