package com.algorhythm.booking_backend.dataproviders.Hotel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelCreationRequest {

    String hotelName;
    MultipartFile hotelImage;
    boolean freeParking;
    boolean freeWiFi;
    boolean freePool;
    boolean freeBreakfast;
}
