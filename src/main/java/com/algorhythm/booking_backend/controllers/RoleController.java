package com.algorhythm.booking_backend.controllers;

import com.algorhythm.booking_backend.entities.Role;
import com.algorhythm.booking_backend.services.interfaces.RoleService;
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

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all roles")
    public ResponseEntity<List<Role>> getAllRoles(){

        List<Role> allRoles = roleService.findAll();
        return ResponseEntity.ok(allRoles);
    }

    @GetMapping("/role")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get a role by its id")
    public ResponseEntity<Role> getRoleById(@RequestParam Integer roleId){
            Role roleFound = roleService.findById(roleId);
            return ResponseEntity.ok(roleFound);
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a role name with its Id")
    public ResponseEntity<Role> updateRole(@RequestParam Integer roleId, @RequestParam String roleName){
            Role role = roleService.updateRole(roleId, roleName);
            return ResponseEntity.ok(role);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a role with a name")
    public ResponseEntity<Role> createRole(@RequestParam String roleName){
            Role role = roleService.createRole(roleName);
            return ResponseEntity.ok(role);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a role by its Id")
    public ResponseEntity<Void> deleteRole(@RequestParam Integer roleId){
            roleService.deleteRoleById(roleId);
            return ResponseEntity.ok(null);
    }
}
