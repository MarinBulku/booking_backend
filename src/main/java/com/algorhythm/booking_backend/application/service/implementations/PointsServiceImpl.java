package com.algorhythm.booking_backend.application.service.implementations;

import com.algorhythm.booking_backend.adapter.in.models.points.PointDto;
import com.algorhythm.booking_backend.application.mapper.PointMapper;
import com.algorhythm.booking_backend.core.entities.Points;
import com.algorhythm.booking_backend.core.entities.Room;
import com.algorhythm.booking_backend.adapter.out.persistence.PointRepository;
import com.algorhythm.booking_backend.adapter.out.persistence.RoomRepository;
import com.algorhythm.booking_backend.application.service.interfaces.PointService;
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
    private static final PointMapper mapper = PointMapper.POINT_MAPPER;

    /*
    * findRoomDiscountsByRoomId(Integer roomId)
    * roomId - Room ID to find all Points for that room
    *
    * Returns a list of Points to that room
    * */
    @Override
    public List<PointDto> findRoomDiscountsByRoomId(Integer roomId) {
        logger.trace("List of Discount points requested for room {}", roomId);
        List<Points> points = pointRepository.findByRoom_RoomId(roomId);
        return mapper.toPointDtoList(points);
    }

    /*
    * getAll()
    * Returns a list of all points of rooms
    * */
    @Override
    public List<PointDto> getAll() {
        List<Points> points = pointRepository.findAll();
        return mapper.toPointDtoList(points);
    }

    /*
    * Adds a Point Entity to specified room
    *
    * If room not found, throws EntityNotFoundException,
    * else returns the Point created
    * */
    @Override
    public PointDto addRoomPoint(Integer roomId, Integer numberOfPoints, Double discount) {

        Room room = roomRepository.findById(roomId).orElseThrow(EntityNotFoundException::new);

        Points newPoint = Points.builder()
                .room(room)
                .requiredPoints(numberOfPoints)
                .discount(discount)
                .build();
        logger.info("Added new room discount point for room {}, with {} points and with discount {}", roomId, numberOfPoints, discount);
        Points p = pointRepository.save(newPoint);
        return mapper.toPointDto(p);
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
