package com.algorhythm.booking_backend.dataproviders.services.implementations;

import com.algorhythm.booking_backend.dataproviders.dtos.authentication.AuthenticationRequest;
import com.algorhythm.booking_backend.dataproviders.dtos.authentication.AuthenticationResponse;
import com.algorhythm.booking_backend.dataproviders.dtos.user.NewUserDto;
import com.algorhythm.booking_backend.dataproviders.dtos.user.UserDto;
import com.algorhythm.booking_backend.dataproviders.dtos.authentication.DeauthenticationRequest;
import com.algorhythm.booking_backend.dataproviders.entities.Role;
import com.algorhythm.booking_backend.dataproviders.entities.User;
import com.algorhythm.booking_backend.core.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.dataproviders.repositories.RoleRepository;
import com.algorhythm.booking_backend.dataproviders.repositories.UserRepository;
import com.algorhythm.booking_backend.dataproviders.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    /*
    * User Service method implementations
    *
    * Repositories to perform crud operations
    * PasswordEncoder to encode user password
    * Jwt Service to deauthenticate a user(revoke the token)
    * */
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    /*
    * findAll()
    *
    * Returns a list of ALL users, in UserDto form
    * */
    @Override
    public List<UserDto> findAll() {

        List<User> allUsers = userRepository.findAll();

        if (allUsers.isEmpty()) return new ArrayList<>();

        ArrayList<UserDto> allUsersDto = new ArrayList<>(allUsers.size());
        for (User u: allUsers) {
            allUsersDto.add(UserDto.builder()
                            .userId(u.getUserId())
                            .name(u.getFullName())
                            .email(u.getEmail())
                            .phoneNumber(u.getPhoneNumber())
                            .address(u.getAddress())
                            .bookingPoints(u.getBookingPoints())
                            .bookingsNumber(u.getBookingsNumber())
                            .role(u.getRole().getRole())
                            .roleId(u.getRole().getRoleId())
                    .build());
        }
        logger.info("List of all users generated");
        return allUsersDto;
    }

    /*
    * findById(Integer id)
    * id - ID of the user to be found
    *
    * If no user is found, EntityNotFoundException is thrown
    * Method returns UserDto with the data of user requested
    * */
    @Override
    public UserDto findById(Integer id) {

        Optional<User> foundUser  = userRepository.findById(id);

        if (foundUser.isEmpty()) {
            throw new EntityNotFoundException("No user with this id: " + id);
        } else {
            User u = foundUser.get();
            logger.info("Found user with id: {}", u.getUserId());
            return UserDto.builder()
                    .userId(u.getUserId())
                    .name(u.getFullName())
                    .email(u.getEmail())
                    .phoneNumber(u.getPhoneNumber())
                    .address(u.getAddress())
                    .bookingPoints(u.getBookingPoints())
                    .bookingsNumber(u.getBookingsNumber())
                    .role(u.getRole().getRole())
                    .roleId(u.getRole().getRoleId())
                    .build();
        }

    }

    /*
    * existsById(Integer id)
    * id - id of user that has to be checked
    *
    * Returns true if user exists with that id in the repository
    * ele returns false
    * */
    @Override
    public boolean existsById(Integer id) {
        return userRepository.existsById(id);
    }

    /*
    * addUser(NewUserDto newUserDto)
    * newUserDto - Request to add new user
    *
    * Adds a new user to repository
    * Returns user entity as UserDto
    * */
    @Override
    public UserDto addUser(NewUserDto newUserDto) {

            Optional<Role> optional = roleRepository.findById(newUserDto.getRoleId());
            if (optional.isEmpty()) throw new EntityNotFoundException("No role with this id: " + newUserDto.getRoleId());

        User newUser = User.builder()
                .fullName(newUserDto.getName())
                .email(newUserDto.getEmail())
                .password(passwordEncoder.encode(newUserDto.getPassword()))
                .phoneNumber(newUserDto.getNumber())
                .role(optional.get())
                .address(newUserDto.getAddress())
                .bookingsNumber(0)
                .bookingPoints(0)
                .build();

        User u = userRepository.save(newUser);
        logger.info("Added user with id: {} as {}", u.getUserId(), u.getRole().getRole());
        return UserDto.builder()
                .userId(u.getUserId())
                .name(u.getFullName())
                .email(u.getEmail())
                .phoneNumber(u.getPhoneNumber())
                .address(u.getAddress())
                .bookingPoints(u.getBookingPoints())
                .bookingsNumber(u.getBookingsNumber())
                .role(u.getRole().getRole())
                .roleId(u.getRole().getRoleId())
                .build();
    }

    /*
    * removeUser(Integer idOfUserToBeRemoved)
    * idOfUserToBeRemoved - ID of the user to be removed
    *
    * If no user is found with that id, throws EntityNotFoundException
    * Else it deletes the user from repository.
    * */
    @Override
    public void removeUser(Integer idOfUserToBeRemoved) {
        if (!userRepository.existsById(idOfUserToBeRemoved)) throw new EntityNotFoundException("No user with this id: "+ idOfUserToBeRemoved);
        logger.info("Removing user with id: {}", idOfUserToBeRemoved);
        userRepository.deleteById(idOfUserToBeRemoved);
    }

    /*
    * authenticate(AuthenticationRequest request)
    * request - The request to authenticate
    *
    * If no user is found with the credentials, EntityNotFoundException is thrown
    * Else, an encrypted token is returned
    * */
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Optional<User> optional = userRepository.findByEmail(request.getEmail());

        if (optional.isEmpty()) {
            optional = userRepository.findByPhoneNumber(request.getEmail());
        }

        if (optional.isEmpty()) throw new EntityNotFoundException("User not found with username: " + request.getEmail());

        User user = optional.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new EntityNotFoundException("Invalid Credentials");
        }

        String jwtToken = jwtService.generateToken(user);
        logger.info("User {} is authenticated, token to be sent is : {}", user.getFullName(), jwtToken);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userId(user.getUserId())
                .name(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .address(user.getAddress())
                .bookingsNumber(user.getBookingsNumber())
                .bookingPoints(user.getBookingPoints())
                .role(user.getRole().getRole())
                .build();
    }

    /*
    * deauthenticate(DeauthenticationRequest request)
    * request - The request to deauthenticate
    *
    * Token sent in the request is revoked
    *
    * If no token is sent, or revocation not made successfully, false is returned
    * Else returned true
    * */
    @Override
    public boolean deauthenticate(DeauthenticationRequest request) {
        if (request.getToken().isBlank()) return false;
        logger.info("Revoking token: {}", request.getToken());
        return jwtService.revokeToken(request.getToken());
    }
}
