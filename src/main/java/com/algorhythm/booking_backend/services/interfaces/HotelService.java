package com.algorhythm.booking_backend.services.interfaces;

import com.algorhythm.booking_backend.dataproviders.Booking.HotelSearchRequest;
import com.algorhythm.booking_backend.dataproviders.Hotel.AvailableHotelDto;
import com.algorhythm.booking_backend.dataproviders.Hotel.HotelCreationRequest;
import com.algorhythm.booking_backend.dataproviders.Hotel.HotelDTO;
import com.algorhythm.booking_backend.entities.Hotel;
import org.springframework.data.domain.Page;

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

    boolean removeHotel(Integer idOfHotelToBeRemoved);
}
