package com.algorhythm.booking_backend.services.implementations;

import com.algorhythm.booking_backend.entities.Points;
import com.algorhythm.booking_backend.repositories.PointRepository;
import com.algorhythm.booking_backend.services.interfaces.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointsServiceImpl implements PointService {

    private final PointRepository pointRepository;

    @Override
    public List<Points> findRoomDiscountsByRoomId(Integer roomId) {

        return pointRepository.findByRoomId(roomId);
    }

    @Override
    public Points addRoomPoint(Integer roomId, Integer numberOfPoints, Double discount) {
        return null;
    }

    @Override
    public void removeRoomPoint(Integer pointId) {

    }
}
