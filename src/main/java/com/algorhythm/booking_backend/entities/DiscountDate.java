package com.algorhythm.booking_backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "discount_date_tbl")
@Builder
@Data
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class DiscountDate {

    /*
    * Discount Date entity
    *
    * Holds the room which has the discount on a specific date,
    * and the discount made on that date
    * */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Integer discountId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_room_id")
    private Room room;

    @Column(name = "discount_date")
    private LocalDate discountDate;

    @Column(name = "discount")
    private Double discount;
}
