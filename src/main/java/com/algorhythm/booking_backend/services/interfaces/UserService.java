package com.algorhythm.booking_backend.services.interfaces;

import com.algorhythm.booking_backend.dataproviders.authentication.AuthenticationRequest;
import com.algorhythm.booking_backend.dataproviders.authentication.AuthenticationResponse;
import com.algorhythm.booking_backend.dataproviders.User.NewUserDto;
import com.algorhythm.booking_backend.dataproviders.User.UserDto;
import com.algorhythm.booking_backend.dataproviders.authentication.DeauthenticationRequest;

import java.util.List;

public interface UserService {

    List<UserDto> findAll();

    UserDto findById(Integer id);

    boolean existsById(Integer id);

    UserDto addUser(NewUserDto newUserDto);

    void removeUser(Integer idOfUserToBeRemoved);

    AuthenticationResponse authenticate(AuthenticationRequest request);
    boolean deauthenticate(DeauthenticationRequest request);
}
