package com.algorhythm.booking_backend.dataproviders.services.interfaces;

import com.algorhythm.booking_backend.dataproviders.dtos.booking.HotelSearchRequest;
import com.algorhythm.booking_backend.dataproviders.dtos.hotel.AvailableHotelDto;
import com.algorhythm.booking_backend.dataproviders.dtos.hotel.HotelCreationRequest;
import com.algorhythm.booking_backend.dataproviders.dtos.hotel.HotelDTO;
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
