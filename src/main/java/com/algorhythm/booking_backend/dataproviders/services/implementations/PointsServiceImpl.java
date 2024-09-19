package com.algorhythm.booking_backend.dataproviders.services.implementations;

import com.algorhythm.booking_backend.dataproviders.entities.Points;
import com.algorhythm.booking_backend.dataproviders.entities.Room;
import com.algorhythm.booking_backend.dataproviders.repositories.PointRepository;
import com.algorhythm.booking_backend.dataproviders.repositories.RoomRepository;
import com.algorhythm.booking_backend.dataproviders.services.interfaces.PointService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    private final Logger logger = LoggerFactory.getLogger(PointsServiceImpl.class);

    /*
    * findRoomDiscountsByRoomId(Integer roomId)
    * roomId - Room ID to find all Points for that room
    *
    * Returns a list of Points to that room
    * */
    @Override
    public List<Points> findRoomDiscountsByRoomId(Integer roomId) {
        logger.trace("List of Discount points requested for room {}", roomId);
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
        logger.info("Added new room discount point for room {}, with {} points and with discount {}", roomId, numberOfPoints, discount);
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
        logger.info("Removed room discount point for pointId {}", pointId);
        pointRepository.deleteById(pointId);
    }
}
