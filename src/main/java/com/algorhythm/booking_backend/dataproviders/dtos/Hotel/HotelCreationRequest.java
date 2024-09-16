package com.algorhythm.booking_backend.dataproviders.dtos.Hotel;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank(message = "Hotel name shouldn't be null")
    private String hotelName;
    @NotBlank(message = "Image file shouldn't be null")
    MultipartFile hotelImage;
    @NotBlank(message = "Owner ID shouldn't be null")
    private Integer ownerId;
    private String freeParking;
    private String freeWiFi;
    private String freePool;
    private String freeBreakfast;
}
