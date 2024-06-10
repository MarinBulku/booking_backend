package com.algorhythm.booking_backend.dataproviders.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    String token;
    Integer userId;
    String name;
    String email;
    String phoneNumber;
    String address;
    Integer bookingsNumber;
    Integer bookingPoints;
    String role;
}
