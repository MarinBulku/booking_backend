package com.algorhythm.booking_backend.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "booking_tbl")
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private Integer bookingId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_room_id")
    private Room room;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "price_paid")
    private Double pricePaid;

    @Column(name = "cancelled")
    private boolean isCancelled;
}
