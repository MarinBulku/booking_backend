package com.algorhythm.booking_backend.repositories;

import com.algorhythm.booking_backend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
