package com.algorhythm.booking_backend.application.mapper;

import com.algorhythm.booking_backend.adapter.in.models.hotel.HotelDTO;
import com.algorhythm.booking_backend.adapter.in.models.hotel.HotelInfoDto;
import com.algorhythm.booking_backend.core.entities.Hotel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface HotelMapper {

    HotelMapper mapper = Mappers.getMapper(HotelMapper.class);

    HotelDTO toHotelDTO(Hotel hotel);

    List<HotelDTO> toHotelDTOList(List<Hotel> hotels);

    @Mapping(target = "ownerId", source = "owner.userId")
    HotelInfoDto toHotelInfoDto(Hotel hotel);

    List<HotelInfoDto> toHotelInfoDtoList(List<Hotel> hotels);
}
