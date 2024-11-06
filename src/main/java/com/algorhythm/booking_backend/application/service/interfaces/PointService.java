package com.algorhythm.booking_backend.application.service.interfaces;

import com.algorhythm.booking_backend.adapter.in.models.points.PointDto;

import java.util.List;

public interface PointService {

    //Points Service method interfaces

    List<PointDto> findRoomDiscountsByRoomId(Integer roomId);

    List<PointDto> getAll();

    PointDto addRoomPoint(Integer roomId, Integer numberOfPoints, Double discount);

    void removeRoomPoint(Integer pointId);
}
