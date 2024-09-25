package com.algorhythm.booking_backend.adapter.in.models.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private Integer userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private Integer bookingsNumber;
    private Integer bookingPoints;
    private String role;
}
