package com.algorhythm.booking_backend.services.interfaces;

import com.algorhythm.booking_backend.entities.DiscountDate;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface DiscountDateService {

    List<DiscountDate> datesBetween(Integer roomId, LocalDate startDate, LocalDate endDate);

    DiscountDate addDiscountDate(Integer roomId, LocalDate date, Double discount);

    void removeDiscountDate(Integer discountDateId);
}