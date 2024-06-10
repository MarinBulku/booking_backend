package com.algorhythm.booking_backend.dataproviders.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewUserDto {
    String name;
    String email;
    String password;
    String number;
    Integer roleId;
    String address;
}
