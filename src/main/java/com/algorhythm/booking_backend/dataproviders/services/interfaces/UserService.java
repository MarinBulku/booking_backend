package com.algorhythm.booking_backend.dataproviders.services.interfaces;

import com.algorhythm.booking_backend.dataproviders.dtos.authentication.AuthenticationRequest;
import com.algorhythm.booking_backend.dataproviders.dtos.authentication.AuthenticationResponse;
import com.algorhythm.booking_backend.dataproviders.dtos.user.NewUserDto;
import com.algorhythm.booking_backend.dataproviders.dtos.user.UserDto;
import com.algorhythm.booking_backend.dataproviders.dtos.authentication.DeauthenticationRequest;

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
