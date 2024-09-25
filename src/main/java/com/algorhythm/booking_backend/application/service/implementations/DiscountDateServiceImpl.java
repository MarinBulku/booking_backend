package com.algorhythm.booking_backend.application.service.implementations;

import com.algorhythm.booking_backend.core.entities.DiscountDate;
import com.algorhythm.booking_backend.core.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.adapter.out.persistence.DiscountDateRepository;
import com.algorhythm.booking_backend.application.service.interfaces.DiscountDateService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountDateServiceImpl implements DiscountDateService {

    //DiscountDate Service method Implementations

    //DiscountDateRepository to get data of DiscountDate from repository
    private final DiscountDateRepository discountDateRepository;
    private final Logger logger = LoggerFactory.getLogger(DiscountDateServiceImpl.class);

    /*
    * datesBetween(Integer roomId, LocalDate startDate, LocalDate endDate)
    * roomId - ID of room to get DiscountDates from
    * startDate - The minimum date that we search discounts from
    * endDate - The maximum date that we search discounts from
    *
    * If found any, it returns a list of DiscountDate objects of the specified room
    * and between the dates specified including both.
    * Otherwise, an empty list is returned
    * */
    @Override
    public List<DiscountDate> datesBetween(Integer roomId, LocalDate startDate, LocalDate endDate) {
        logger.trace("List of dates required from {} to {} for room with id {}", startDate, endDate, roomId);
        return discountDateRepository.findByDiscountDateBetweenAndRoom_RoomId(startDate, endDate, roomId);
    }

    /*
    * addDiscountDate(Integer roomId, LocalDate date, Double discount)
    * roomId - ID of room we want to add a discount date to
    * date - date that the discount will take place
    * discount - discount of price on that day
    *
    * If saved successfully, it returns the new DiscountDate created in the repository,
    * else null is returned
    * */
    @Override
    public DiscountDate addDiscountDate(Integer roomId, LocalDate date, Double discount) {
        DiscountDate newDate = DiscountDate.builder()
                .discountDate(date)
                .discount(discount)
                .build();

        logger.trace("Adding discount date {} for room with id {}", date, roomId);
        return discountDateRepository.save(newDate);
    }

    /*
     * removeDiscountDate(Integer discountDateId)
     * discountDateId - ID of DiscountDate that needs to be removed
     * If there isn't any DiscountDate with that ID, an EntityNotFoundException is thrown,
     * else remove the object from repository.
    * */
    @Override
    public void removeDiscountDate(Integer discountDateId) {
        if (!discountDateRepository.existsById(discountDateId)) throw new EntityNotFoundException("No discount date with this id: " + discountDateId);
        logger.trace("Removing discount date with id {}", discountDateId);
        discountDateRepository.deleteById(discountDateId);
    }
}
