package com.algorhythm.booking_backend.dataproviders.dtos.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = "Email shouldn't be null")
    private String email;
    @NotBlank(message = "Password shouldn't be null")
    private String password;
}
