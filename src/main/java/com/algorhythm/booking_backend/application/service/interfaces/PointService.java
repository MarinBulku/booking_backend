package com.algorhythm.booking_backend.application.service.interfaces;

import com.algorhythm.booking_backend.core.entities.Points;

import java.util.List;

public interface PointService {

    //Points Service method interfaces

    List<Points> findRoomDiscountsByRoomId(Integer roomId);

    Points addRoomPoint(Integer roomId, Integer numberOfPoints, Double discount);

    void removeRoomPoint(Integer pointId);
}
