package com.algorhythm.booking_backend.adapter.in.models.room;

import lombok.*;

import java.util.ArrayList;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AvailableRoomDto {
    private Integer roomId;
    private String roomName;
    private String description;
    private String roomImage;
    private ArrayList<DatePrice> datePriceList;
    private Double totalPrice;
    private Double pointsDiscount;
}
