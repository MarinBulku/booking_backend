package com.algorhythm.booking_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "room_tbl")
@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "room_name", nullable = false)
    private String roomName;

    @Column(name = "capacity_adults")
    private Integer adultsCapacity;

    @Column(name = "capacity_kids")
    private Integer kidsCapacity;

    @Column(name = "price")
    private Double price;

    @Column(name = "description")
    private String description;

    @Column(name = "image_path")
    private String roomImagePath;

    @ManyToOne
    @JoinColumn(name = "fk_hotel_id")
    private Hotel hotel;
}
