package com.algorhythm.booking_backend.services.implementations;

import com.algorhythm.booking_backend.dataproviders.authentication.AuthenticationRequest;
import com.algorhythm.booking_backend.dataproviders.authentication.AuthenticationResponse;
import com.algorhythm.booking_backend.dataproviders.User.NewUserDto;
import com.algorhythm.booking_backend.dataproviders.User.UserDto;
import com.algorhythm.booking_backend.dataproviders.authentication.DeauthenticationRequest;
import com.algorhythm.booking_backend.entities.Role;
import com.algorhythm.booking_backend.entities.User;
import com.algorhythm.booking_backend.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.repositories.RoleRepository;
import com.algorhythm.booking_backend.repositories.UserRepository;
import com.algorhythm.booking_backend.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
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
        return allUsersDto;
    }

    @Override
    public UserDto findById(Integer id) {

        Optional<User> foundUser  = userRepository.findById(id);

        if (foundUser.isPresent()) {
            User u = foundUser.get();
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

        else throw new EntityNotFoundException("No user with this id: " + id);

    }

    @Override
    public boolean existsById(Integer id) {
        return userRepository.existsById(id);
    }

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

    @Override
    public void removeUser(Integer idOfUserToBeRemoved) {
        if (!userRepository.existsById(idOfUserToBeRemoved)) throw new EntityNotFoundException("No user with this id: "+ idOfUserToBeRemoved);
        userRepository.deleteById(idOfUserToBeRemoved);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Optional<User> optional = userRepository.findByEmail(request.getEmail());

        if (optional.isEmpty()) {
            throw new EntityNotFoundException("User not found with email: " + request.getEmail());
        }

        User user = optional.get();
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new EntityNotFoundException("Invalid Credentials");
        }

        String jwtToken = jwtService.generateToken(user);
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

    @Override
    public boolean deauthenticate(DeauthenticationRequest request) {
        return jwtService.revokeToken(request.getToken());
    }
}
