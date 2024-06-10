package com.algorhythm.booking_backend.dataproviders.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    Integer userId;
    String name;
    String email;
    String phoneNumber;
    String address;
    Integer bookingsNumber;
    Integer bookingPoints;
    Integer roleId;
    String role;
}
