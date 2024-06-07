package com.algorhythm.booking_backend.datasources.datatansferobjects.authentication;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeauthenticationRequest {
    @NotBlank(message = "Token shouldn't be null")
    private String token;
}
