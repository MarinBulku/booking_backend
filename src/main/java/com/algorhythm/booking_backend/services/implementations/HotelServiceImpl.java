package com.algorhythm.booking_backend.services.implementations;

import com.algorhythm.booking_backend.entities.Hotel;
import com.algorhythm.booking_backend.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.repositories.HotelRepository;
import com.algorhythm.booking_backend.services.interfaces.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    @Override
    public List<Hotel> findAll() {
        List<Hotel> allHotels = hotelRepository.findAll();

        if (allHotels.isEmpty()) return new ArrayList<>();

        return allHotels;
    }

    @Override
    public Hotel findById(Integer id) {
        Optional<Hotel> optional = hotelRepository.findById(id);

        if (optional.isEmpty()) throw new EntityNotFoundException("No hotel with this id: " + id);

        return optional.get();
    }

    @Override
    public boolean existsById(Integer id) {
        return hotelRepository.existsById(id);
    }

    @Override
    public Hotel addHotel(String name, String imagePath, boolean freeParking, boolean freeWifi, boolean freePool, boolean freeBreakfast) {

        Hotel newHotel = Hotel.builder()
                .hotelName(name)
                .hotelImagePath(imagePath)
                .freeParking(freeParking)
                .freeWiFi(freeWifi)
                .freePool(freePool)
                .freeBreakfast(freeBreakfast)
                .build();

        return hotelRepository.save(newHotel);
    }

    @Override
    public void removeHotel(Integer idOfHotelToBeRemoved) {
        if (!hotelRepository.existsById(idOfHotelToBeRemoved)) throw new EntityNotFoundException("No hotel with this id: " + idOfHotelToBeRemoved);
        hotelRepository.deleteById(idOfHotelToBeRemoved);
    }
}
