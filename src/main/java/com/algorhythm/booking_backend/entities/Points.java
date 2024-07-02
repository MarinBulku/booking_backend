package com.algorhythm.booking_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "point_tbl")
@Builder
@Data
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Points {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Integer pointId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_room_id")
    private Room room;

    @Column(name = "points_required")
    private Integer requiredPoints;

    @Column(name = "discount")
    private Double discount;
}
