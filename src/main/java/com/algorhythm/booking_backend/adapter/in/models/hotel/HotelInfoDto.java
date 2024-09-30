package com.algorhythm.booking_backend.adapter.in.models.hotel;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HotelInfoDto {

     Integer hotelId;

     String hotelName;

     String hotelImagePath;

     boolean freeParking;
    
     boolean freeWiFi;

     boolean freePool;

     boolean freeBreakfast;
    
     Integer ownerId;
}
