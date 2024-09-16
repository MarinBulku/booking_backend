package com.algorhythm.booking_backend.dataproviders.repositories;

import com.algorhythm.booking_backend.dataproviders.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    /*
    * Role Repository, to perform simple CRUD operations on
    * */
}
