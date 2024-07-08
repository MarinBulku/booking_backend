package com.algorhythm.booking_backend.services.interfaces;

import com.algorhythm.booking_backend.entities.Role;

import java.util.List;

public interface RoleService {

    //Role Service method interfaces

    List<Role> findAll();

    Role findById(Integer roleId);

    Role createRole(String roleName);

    boolean existsById(Integer roleId);

    Role updateRole(Integer idOfTheRoleToBeUpdated, String newRoleName);

    void deleteRoleById(Integer idOfRoleToBeDeleted);
}
