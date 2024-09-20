package com.algorhythm.booking_backend.core.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_tbl")
@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Integer roleId;

    @Column(name = "role", nullable = false)
    private String role;

}
