package com.algorhythm.booking_backend.adapter.in.web;

import com.algorhythm.booking_backend.adapter.in.models.points.PointDto;
import com.algorhythm.booking_backend.application.service.interfaces.PointService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/points")
@Tag(
        name = "Points",
        description = "All apis of Points"
)
public class PointController {

    private final PointService pointService;

    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<PointDto>> getAllPoints(){
        return ResponseEntity.ok(pointService.getAll());
    }

    @GetMapping("/points-of-room")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<PointDto>> findRoomDiscountPoints(@RequestParam Integer roomId){
        return ResponseEntity.ok(pointService.findRoomDiscountsByRoomId(roomId));
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PointDto> addRoomPoint(@RequestParam Integer roomId, @RequestParam Integer numberOfPoints, @RequestParam Double discount){
        PointDto newPoint = pointService.addRoomPoint(roomId, numberOfPoints, discount);
        return ResponseEntity.ok(newPoint);
    }

    @DeleteMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePointOfRoom(@RequestParam Integer pointId){
        pointService.removeRoomPoint(pointId);
        return ResponseEntity.ok("Deleted successfully");
    }
}
