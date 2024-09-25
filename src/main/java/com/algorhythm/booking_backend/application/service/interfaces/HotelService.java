package com.algorhythm.booking_backend.application.service.interfaces;

import com.algorhythm.booking_backend.adapter.in.models.booking.HotelSearchRequest;
import com.algorhythm.booking_backend.adapter.in.models.hotel.AvailableHotelDto;
import com.algorhythm.booking_backend.adapter.in.models.hotel.HotelCreationRequest;
import com.algorhythm.booking_backend.adapter.in.models.hotel.HotelDTO;
import com.algorhythm.booking_backend.core.entities.Hotel;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;

public interface HotelService {
    //Hotel Service method interfaces
    List<Hotel> findAll();

    List<HotelDTO> allHotelDtos();

    List<HotelDTO> allHotelDtosByOwner(Integer ownerId);

    Page<AvailableHotelDto> findAllAvailableHotels(HotelSearchRequest request, Integer pageNo) throws Exception;

    Hotel findById(Integer id);

    boolean existsById(Integer id);

    boolean addHotel(HotelCreationRequest request);

    void removeHotel(Integer idOfHotelToBeRemoved) throws IOException;
}
