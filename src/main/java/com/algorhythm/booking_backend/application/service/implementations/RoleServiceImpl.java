package com.algorhythm.booking_backend.application.service.implementations;

import com.algorhythm.booking_backend.adapter.in.models.role.RoleDto;
import com.algorhythm.booking_backend.application.mapper.RoleMapper;
import com.algorhythm.booking_backend.core.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.core.entities.Role;
import com.algorhythm.booking_backend.adapter.out.persistence.RoleRepository;
import com.algorhythm.booking_backend.application.service.interfaces.RoleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    /*
    * Role Service method implementations
    * Role Repository to perform CRUD operations
    * */
    private final RoleRepository roleRepository;
    private final Logger logger = LoggerFactory.getLogger(RoleServiceImpl.class);
    private static final RoleMapper mapper = RoleMapper.ROLE_MAPPER;

    /*
    * findAll() - No parameters
    *
    * Return a list of ALL roles
    * */
    @Override
    public List<RoleDto> findAll() {
        logger.trace("List of all roles requested");
        List<Role> allRoles = roleRepository.findAll();

        return mapper.toDtoList(allRoles);
    }

    /*
    * findById(Integer roleId)
    * roleId - ID of role to be found
    *
    * If no role is found, throws EntityNotFoundException,
    * else returns the role
    * */
    @Override
    public RoleDto findById(Integer roleId){
        logger.trace("Requesting role with id {}", roleId);
        Role roleFound = roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found"));

        return mapper.toDto(roleFound);
    }

    /*
    * createRole(String roleName)
    * roleName - Name of the role to be created
    *
    * Returns the new Role
    * */
    @Override
    public RoleDto createRole(String roleName) {
        Role newRole = Role.builder()
                .role(roleName)
                .build();
        logger.info("New role {} being saved", newRole.getRole());
        Role r = roleRepository.save(newRole);
        return mapper.toDto(r);
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
    public RoleDto updateRole(Integer idOfTheRoleToBeUpdated, String newRoleName) {
        if (!roleRepository.existsById(idOfTheRoleToBeUpdated))
            throw new EntityNotFoundException("No role with this ID: " + idOfTheRoleToBeUpdated);

        Role role = roleRepository.findById(idOfTheRoleToBeUpdated).orElseThrow(() -> new EntityNotFoundException("No role with this id:" + idOfTheRoleToBeUpdated));

        role.setRole(newRoleName);
        logger.info("Updating role with id {}", idOfTheRoleToBeUpdated);
        Role updated = roleRepository.save(role);
        return mapper.toDto(updated);
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
        logger.info("Deleting role with id {}", idOfRoleToBeDeleted);
        roleRepository.deleteById(idOfRoleToBeDeleted);
    }
}
