package com.algorhythm.booking_backend.datasources.datatansferobjects.room;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DatePrice {
    private LocalDate date;
    private Double price;
}
