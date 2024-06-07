package com.algorhythm.booking_backend.datasources.services.interfaces;

import com.algorhythm.booking_backend.core.entities.DiscountDate;

import java.time.LocalDate;
import java.util.List;

public interface DiscountDateService {
    //Discount Date Service method interfaces
    List<DiscountDate> datesBetween(Integer roomId, LocalDate startDate, LocalDate endDate);

    DiscountDate addDiscountDate(Integer roomId, LocalDate date, Double discount);

    void removeDiscountDate(Integer discountDateId);
}
