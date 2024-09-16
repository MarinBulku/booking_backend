package com.algorhythm.booking_backend.dataproviders.services.implementations;

import com.algorhythm.booking_backend.core.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.dataproviders.entities.Role;
import com.algorhythm.booking_backend.dataproviders.repositories.RoleRepository;
import com.algorhythm.booking_backend.dataproviders.services.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    /*
    * Role Service method implementations
    * Role Repository to perform CRUD operations
    * */
    private final RoleRepository roleRepository;

    /*
    * findAll() - No parameters
    *
    * Return a list of ALL roles
    * */
    @Override
    public List<Role> findAll() {

        return roleRepository.findAll();
    }

    /*
    * findById(Integer roleId)
    * roleId - ID of role to be found
    *
    * If no role is found, throws EntityNotFoundException,
    * else returns the role
    * */
    @Override
    public Role findById(Integer roleId){

        Optional<Role> roleFound = roleRepository.findById(roleId);

        return roleFound.orElseThrow(() -> new EntityNotFoundException("No role with this ID: " + roleId));

    }

    /*
    * createRole(String roleName)
    * roleName - Name of the role to be created
    *
    * Returns the new Role
    * */
    @Override
    public Role createRole(String roleName) {
        Role newRole = Role.builder()
                .role(roleName)
                .build();

        return roleRepository.save(newRole);
    }

    /*
    * existsById(Integer roleId)
    * roleId - ID of role to check
    *
    * If a role exists, returns true, otherwise false.
    * */
    @Override
    public boolean existsById(Integer roleId) {

        return roleRepository.existsById(roleId);
    }

    /*
    * updateRole(Integer idOfTheRoleToBeUpdated, String newRoleName)
    * idOfTheRoleToBeUpdated - ID of the role to be updated
    * newRoleName - New name of the updated role
    *
    * If a role does not exist by the given id, throws EntityNotFoundException,
    * else save and return the updated role
    * */
    @Override
    public Role updateRole(Integer idOfTheRoleToBeUpdated, String newRoleName) {
        if (!roleRepository.existsById(idOfTheRoleToBeUpdated))
            throw new EntityNotFoundException("No role with this ID: " + idOfTheRoleToBeUpdated);

        Role role = roleRepository.findById(idOfTheRoleToBeUpdated).orElseThrow(() -> new EntityNotFoundException("No role with this id:" + idOfTheRoleToBeUpdated));

        role.setRole(newRoleName);
        return roleRepository.save(role);
    }

    /*
    * deleteRoleById(Integer idOfRoleToBeDeleted)
    * idOfTheRoleToBeDeleted - ID of the role that will be deleted
    *
    * If there isn't any role with that ID, it throws EntityNotFoundException,
    * else it deletes the role
    * */
    @Override
    public void deleteRoleById(Integer idOfRoleToBeDeleted) {
        if (!roleRepository.existsById(idOfRoleToBeDeleted))
            throw new EntityNotFoundException("No role with this ID: " + idOfRoleToBeDeleted);
        roleRepository.deleteById(idOfRoleToBeDeleted);
    }
}
