package com.algorhythm.booking_backend.dataproviders.dtos.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer userId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private Integer bookingsNumber;
    private Integer bookingPoints;
    private Integer roleId;
    private String role;
}
