package com.algorhythm.booking_backend.services.interfaces;

import com.algorhythm.booking_backend.entities.Hotel;

import java.util.List;

public interface HotelService {

    List<Hotel> findAll();

    Hotel findById(Integer id);

    boolean existsById(Integer id);

    Hotel addHotel(String name, String imagePath, boolean freeParking, boolean freeWifi, boolean freePool, boolean freeBreakfast);

    void removeHotel(Integer idOfHotelToBeRemoved);
}
