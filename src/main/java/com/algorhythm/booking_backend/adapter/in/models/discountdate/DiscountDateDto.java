package com.algorhythm.booking_backend.adapter.in.models.discountdate;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DiscountDateDto {
    private Integer discountId;
    private Integer roomId;
    private LocalDate discountDate;
    private Double discount;
}
