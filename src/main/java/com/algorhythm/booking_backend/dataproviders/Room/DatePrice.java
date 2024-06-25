package com.algorhythm.booking_backend.dataproviders.Room;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
public class DatePrice {
    private LocalDate date;
    private Double price;
}
