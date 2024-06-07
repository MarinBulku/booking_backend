package com.algorhythm.booking_backend.services.implementations;

import com.algorhythm.booking_backend.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.entities.Role;
import com.algorhythm.booking_backend.repositories.RoleRepository;
import com.algorhythm.booking_backend.services.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public List<Role> findAll() {

        List<Role> allRoles = roleRepository.findAll();

        if (allRoles.isEmpty()) return new ArrayList<Role>();

        return allRoles;
    }

    @Override
    public Role findById(Integer roleId){

        Optional<Role> roleFound = roleRepository.findById(roleId);

        return roleFound.orElseThrow(() -> new EntityNotFoundException("No role with this ID: " + roleId));

    }

    @Override
    public boolean existsById(Integer roleId) {

        return roleRepository.existsById(roleId);
    }

    @Override
    public Role updateRole(Integer idOfTheRoleToBeUpdated, String newRoleName) {
        if (!roleRepository.existsById(idOfTheRoleToBeUpdated))
            throw new EntityNotFoundException("No role with this ID: " + idOfTheRoleToBeUpdated);

        Role role = roleRepository.findById(idOfTheRoleToBeUpdated).get();

        role.setRole(newRoleName);
        return roleRepository.save(role);
    }

    @Override
    public void deleteRoleById(Integer idOfRoleToBeDeleted) {
        if (!roleRepository.existsById(idOfRoleToBeDeleted))
            throw new EntityNotFoundException("No role with this ID: " + idOfRoleToBeDeleted);
        roleRepository.deleteById(idOfRoleToBeDeleted);
    }
}
