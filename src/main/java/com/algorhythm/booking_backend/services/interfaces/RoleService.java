package com.algorhythm.booking_backend.services.interfaces;

import com.algorhythm.booking_backend.entities.Role;

import java.util.List;

public interface RoleService {

    List<Role> findAll();

    Role findById(Integer roleId);

    boolean existsById(Integer roleId);

    Role updateRole(Integer idOfTheRoleToBeUpdated, String newRoleName);

    void deleteRoleById(Integer idOfRoleToBeDeleted);
}