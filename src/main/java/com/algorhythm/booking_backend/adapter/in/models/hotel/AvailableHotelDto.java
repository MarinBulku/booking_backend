package com.algorhythm.booking_backend.adapter.in.models.hotel;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AvailableHotelDto {
    private Integer hotelId;
    private String hotelName;
    private String hotelImage;
    private boolean freeParking;
    private boolean freeWiFi;
    private boolean freePool;
    private boolean freeBreakfast;
    private Integer roomCount;
}
