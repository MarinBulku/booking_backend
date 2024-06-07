package com.algorhythm.booking_backend.controllers;

import com.algorhythm.booking_backend.core.entities.Role;
import com.algorhythm.booking_backend.datasources.services.interfaces.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/roles")
@Tag(
        name = "Roles endpoints",
        description = "All apis of role"
)
public class RoleController {

    private final RoleService roleService;

    /*
     * getAllRoles()
     *
     * Must be an admin to access this endpoint
     * Returns a list of all roles in the system
     *
     * If it encounters an error, by default a response with bad_request status is returned
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all roles")
    public ResponseEntity<List<Role>> getAllRoles(){
        List<Role> allRoles = roleService.findAll();
        return ResponseEntity.ok(allRoles);
    }

    /*
     * getRoleById(Integer roleId)
     * roleId - ID of the role to fetch
     *
     * Must be an admin to access this endpoint
     * Returns the role corresponding to the specified ID
     *
     * If it encounters an error, by default a response with bad_request status is returned
     */
    @GetMapping("/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get a role by its id")
    public ResponseEntity<Role> getRoleById(@RequestParam Integer roleId){
        Role roleFound = roleService.findById(roleId);
        return ResponseEntity.ok(roleFound);
    }

    /*
     * updateRole(Integer roleId, String roleName)
     * roleId - ID of the role to be updated
     * roleName - New name for the role
     *
     * Must be an admin to access this endpoint
     * Returns the updated role
     *
     * If it encounters an error, by default a response with bad_request status is returned
     */
    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a role name with its Id")
    public ResponseEntity<Role> updateRole(@RequestParam Integer roleId, @RequestParam String roleName){
        Role role = roleService.updateRole(roleId, roleName);
        return ResponseEntity.ok(role);
    }

    /*
     * createRole(String roleName)
     * roleName - Name of the new role to be created
     *
     * Must be an admin to access this endpoint
     * Returns the newly created role
     *
     * If it encounters an error, by default a response with bad_request status is returned
     */
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a role with a name")
    public ResponseEntity<Role> createRole(@RequestParam String roleName){
        Role role = roleService.createRole(roleName);
        return ResponseEntity.ok(role);
    }

    /*
     * deleteRole(Integer roleId)
     * roleId - ID of the role to be deleted
     *
     * Must be an admin to access this endpoint
     * Returns a response indicating the success of the role deletion
     *
     * If it encounters an error, by default a response with bad_request status is returned
     */
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a role by its Id")
    public ResponseEntity<String> deleteRole(@RequestParam Integer roleId){
            roleService.deleteRoleById(roleId);
            return ResponseEntity.ok("Role deleted successfully");
    }
}
