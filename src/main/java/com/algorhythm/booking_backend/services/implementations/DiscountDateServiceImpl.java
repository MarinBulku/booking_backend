package com.algorhythm.booking_backend.services.implementations;

import com.algorhythm.booking_backend.entities.DiscountDate;
import com.algorhythm.booking_backend.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.repositories.DiscountDateRepository;
import com.algorhythm.booking_backend.repositories.RoomRepository;
import com.algorhythm.booking_backend.services.interfaces.DiscountDateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountDateServiceImpl implements DiscountDateService {

    private final DiscountDateRepository discountDateRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<DiscountDate> datesBetween(Integer roomId, LocalDate startDate, LocalDate endDate) {

        List<DiscountDate> allDiscountDates = discountDateRepository.findByDiscountDateBetweenAndRoom_RoomId(startDate, endDate, roomId);

        if (allDiscountDates.isEmpty()) return new ArrayList<>();

        return allDiscountDates;
    }

    @Override
    public DiscountDate addDiscountDate(Integer roomId, LocalDate date, Double discount) {
        DiscountDate newDate = DiscountDate.builder()
                .discountDate(date)
                .discount(discount)
                .build();

        return discountDateRepository.save(newDate);
    }

    @Override
    public void removeDiscountDate(Integer discountDateId) {
        if (!discountDateRepository.existsById(discountDateId)) throw new EntityNotFoundException("No discount date with this id: " + discountDateId);
        discountDateRepository.deleteById(discountDateId);
    }
}
