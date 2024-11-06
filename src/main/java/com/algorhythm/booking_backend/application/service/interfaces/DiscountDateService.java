package com.algorhythm.booking_backend.application.service.interfaces;

import com.algorhythm.booking_backend.adapter.in.models.discountdate.DiscountDateDto;
import com.algorhythm.booking_backend.core.entities.DiscountDate;

import java.time.LocalDate;
import java.util.List;

public interface DiscountDateService {
    //Discount Date Service method interfaces
    List<DiscountDate> datesBetween(Integer roomId, LocalDate startDate, LocalDate endDate);

    List<DiscountDateDto> getRoomDiscountDates(Integer roomId);

    DiscountDateDto addDiscountDate(Integer roomId, LocalDate date, Double discount);

    void removeDiscountDate(Integer discountDateId);
}
