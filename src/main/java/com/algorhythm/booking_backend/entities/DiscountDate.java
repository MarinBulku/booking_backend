package com.algorhythm.booking_backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "discount_date_tbl")
@Builder
@Data
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class DiscountDate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "discount_id")
    private Integer discountId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_room_id")
    private Room room;

    @Column(name = "discount_date")
    private Date discountDate;

    @Column(name = "discount")
    private Double discount;
}
