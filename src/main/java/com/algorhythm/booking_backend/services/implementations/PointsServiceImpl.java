package com.algorhythm.booking_backend.services.implementations;

import com.algorhythm.booking_backend.entities.Points;
import com.algorhythm.booking_backend.entities.Room;
import com.algorhythm.booking_backend.repositories.PointRepository;
import com.algorhythm.booking_backend.repositories.RoomRepository;
import com.algorhythm.booking_backend.services.interfaces.PointService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointsServiceImpl implements PointService {

    /*
    * Points Service method implementations
    * Point Repository to make CRUD operations
    * */
    private final PointRepository pointRepository;
    private final RoomRepository roomRepository;

    /*
    * findRoomDiscountsByRoomId(Integer roomId)
    * roomId - Room ID to find all Points for that room
    *
    * Returns a list of Points to that room
    * */
    @Override
    public List<Points> findRoomDiscountsByRoomId(Integer roomId) {

        return pointRepository.findByRoom_RoomId(roomId);
    }

    /*
    * Adds a Point Entity to specified room
    *
    * If room not found, throws EntityNotFoundException,
    * else returns the Point created
    * */
    @Override
    public Points addRoomPoint(Integer roomId, Integer numberOfPoints, Double discount) {

        Room room = roomRepository.findById(roomId).orElseThrow(EntityNotFoundException::new);

        Points newPoint = Points.builder()
                .room(room)
                .requiredPoints(numberOfPoints)
                .discount(discount)
                .build();

        return pointRepository.save(newPoint);
    }

    /*
    * removeRoomPoint(Integer pointId)
    * pointId - ID of the point to be removed
    *
    * If there exists a point with that id, it removes it
    * else it throws EntityNotFoundException
    * */
    @Override
    public void removeRoomPoint(Integer pointId) {
        if (!pointRepository.existsById(pointId)) throw new EntityNotFoundException("No points with this id:" + pointId);
        pointRepository.deleteById(pointId);
    }
}
