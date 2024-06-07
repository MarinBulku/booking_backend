package com.algorhythm.booking_backend.services.interfaces;

import com.algorhythm.booking_backend.entities.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User findById(Integer id);

    boolean existsById(Integer id);

    User addCustomer(String name, String email, String password, String number, Integer roleId, String Address);

    void removeCustomer(Integer idOfUserToBeRemoved);
}
