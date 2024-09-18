package com.algorhythm.booking_backend.dataproviders.dtos.room;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DatePrice {
    private LocalDate date;
    private Double price;
}
