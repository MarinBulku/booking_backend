package com.algorhythm.booking_backend.application.mapper;

import com.algorhythm.booking_backend.adapter.in.models.points.PointDto;
import com.algorhythm.booking_backend.core.entities.Points;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PointMapper {

    PointMapper POINT_MAPPER = Mappers.getMapper(PointMapper.class);

    @Mapping(target = "roomId", source = "room.roomId")
    PointDto toPointDto(Points point);

    List<PointDto> toPointDtoList(List<Points> pointsList);

}
