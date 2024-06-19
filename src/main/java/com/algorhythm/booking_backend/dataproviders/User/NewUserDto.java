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
    private String name;
    private String email;
    private String password;
    private String number;
    private Integer roleId;
    private String address;
}
