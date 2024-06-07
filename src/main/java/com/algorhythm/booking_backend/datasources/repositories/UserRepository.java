package com.algorhythm.booking_backend.datasources.repositories;

import com.algorhythm.booking_backend.core.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    /*
    * findByEmail(String email)
    * email - Email of the user to be found
    *
    * Returns an optional object of the user found by email
    * */
    Optional<User> findByEmail(String email);

    /*
     * findByPhoneNumber(String phoneNumber)
     * phoneNumber - Phone number of the user to be found
     *
     * Returns an optional object of the user found by phone number
     * */
    Optional<User> findByPhoneNumber(String phoneNumber);
}
