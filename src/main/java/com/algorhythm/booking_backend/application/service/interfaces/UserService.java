package com.algorhythm.booking_backend.application.service.interfaces;

import com.algorhythm.booking_backend.adapter.in.models.authentication.AuthenticationRequest;
import com.algorhythm.booking_backend.adapter.in.models.authentication.AuthenticationResponse;
import com.algorhythm.booking_backend.adapter.in.models.user.NewUserDto;
import com.algorhythm.booking_backend.adapter.in.models.user.UserDto;
import com.algorhythm.booking_backend.adapter.in.models.authentication.DeauthenticationRequest;

import java.util.List;

public interface UserService {

    //User Service method interfaces

    List<UserDto> findAll();

    UserDto findById(Integer id);

    boolean existsById(Integer id);

    UserDto addUser(NewUserDto newUserDto);

    void removeUser(Integer idOfUserToBeRemoved);

    AuthenticationResponse authenticate(AuthenticationRequest request);
    boolean deauthenticate(DeauthenticationRequest request);
}
