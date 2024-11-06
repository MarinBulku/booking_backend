package com.algorhythm.booking_backend.adapter.in.models.points;

import lombok.Data;

@Data
public class PointDto {

    private Integer pointId;
    private Integer roomId;
    private Integer requiredPoints;
    private Double discount;
}
