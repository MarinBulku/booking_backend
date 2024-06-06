package com.algorhythm.booking_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_tbl")
@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "bookings_number")
    private String bookingsNumber;

    @Column(name = "booking_points")
    private String bookingPoints;

    @ManyToOne
    @JoinColumn(name = "fk_role_id")
    private Role role;
}
