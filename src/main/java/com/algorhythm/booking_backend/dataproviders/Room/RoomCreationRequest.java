package com.algorhythm.booking_backend.dataproviders.Room;

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
    String roomName;
    Integer adultCapacity;
    Integer kidCapacity;
    Integer price;
    String description;
    Integer hotelId;
    MultipartFile roomImage;
}
