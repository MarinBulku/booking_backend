package com.algorhythm.booking_backend.application.service.interfaces;

import com.algorhythm.booking_backend.adapter.in.models.role.RoleDto;

import java.util.List;

public interface RoleService {

    //Role Service method interfaces

    List<RoleDto> findAll();

    RoleDto findById(Integer roleId);

    RoleDto createRole(String roleName);

    boolean existsById(Integer roleId);

    RoleDto updateRole(Integer idOfTheRoleToBeUpdated, String newRoleName);

    void deleteRoleById(Integer idOfRoleToBeDeleted);
}
