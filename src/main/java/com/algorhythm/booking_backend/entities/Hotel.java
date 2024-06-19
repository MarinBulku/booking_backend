package com.algorhythm.booking_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "hotel_tbl")
@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id", nullable = false)
    private Integer hotelId;

    @Column(name = "hotel_name", nullable = false)
    private String hotelName;

    @Column(name = "image_path")
    private String hotelImagePath;

    @Column(name = "free_parking")
    private boolean freeParking;

    @Column(name = "free_wifi")
    private boolean freeWiFi;

    @Column(name = "free_pool")
    private boolean freePool;

    @Column(name = "free_breakfast")
    private boolean freeBreakfast;

    @ManyToOne
    @JoinColumn(name = "fk_owner_id")
    private User owner;
}
