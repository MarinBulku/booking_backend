package com.algorhythm.booking_backend.adapter.in.models.role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class RoleDto {

    Integer roleId;
    String role;
}
