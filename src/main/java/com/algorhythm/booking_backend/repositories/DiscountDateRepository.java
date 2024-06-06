package com.algorhythm.booking_backend.repositories;

import com.algorhythm.booking_backend.entities.DiscountDate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountDateRepository extends JpaRepository<DiscountDate, Integer> {
}
