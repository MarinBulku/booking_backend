package com.algorhythm.booking_backend.dataproviders.Hotel;

import jakarta.persistence.Column;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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
}
