package com.algorhythm.booking_backend.application.mapper;

import com.algorhythm.booking_backend.adapter.in.models.role.RoleDto;
import com.algorhythm.booking_backend.core.entities.Role;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface RoleMapper {

    RoleMapper ROLE_MAPPER = Mappers.getMapper(RoleMapper.class);

    RoleDto toDto(Role role);

    List<RoleDto> toDtoList(List<Role> roles);
}
