package com.algorhythm.booking_backend.adapter.in.models.hotel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelDTO {
    private Integer hotelId;
    private String hotelName;
}
