package com.algorhythm.booking_backend.application.mapper;

import com.algorhythm.booking_backend.adapter.in.models.discountdate.DiscountDateDto;
import com.algorhythm.booking_backend.core.entities.DiscountDate;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface DiscountDateMapper {

    DiscountDateMapper mapper = Mappers.getMapper(DiscountDateMapper.class);

    @Mapping(target = "roomId", source = "room.roomId")
    DiscountDateDto toDto(DiscountDate discountDate);

    List<DiscountDateDto> toDtoList(List<DiscountDate> dates);
}