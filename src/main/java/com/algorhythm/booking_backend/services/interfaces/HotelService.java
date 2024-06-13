package com.algorhythm.booking_backend.services.interfaces;

import com.algorhythm.booking_backend.dataproviders.Hotel.HotelCreationRequest;
import com.algorhythm.booking_backend.dataproviders.Hotel.HotelDTO;
import com.algorhythm.booking_backend.entities.Hotel;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface HotelService {

    List<Hotel> findAll();

    List<HotelDTO> allHotelDtos();

    Hotel findById(Integer id);

    boolean existsById(Integer id);

    boolean addHotel(HotelCreationRequest request);

    boolean removeHotel(Integer idOfHotelToBeRemoved);
}
