package com.algorhythm.booking_backend.dataproviders.Room;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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
public class RoomCreationRequest {
    @NotBlank(message = "Room name shouldn't be null")
    private String roomName;
    @NotBlank(message = "Adult capacity should not be null, min = 1")
    @Min(1)
    private Integer adultCapacity;
    @NotBlank(message = "Kids capacity should not be null")
    @Min(0)
    private Integer kidCapacity;
    @NotBlank(message = "Price should not be null")
    @DecimalMin("0.0")
    private Double price;
    private String description;
    @NotBlank(message = "Hotel ID should not be null")
    private Integer hotelId;
    @NotBlank(message = "Image file should not be null")
    private MultipartFile roomImage;
}
