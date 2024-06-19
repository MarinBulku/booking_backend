package com.algorhythm.booking_backend.controllers;

import com.algorhythm.booking_backend.dataproviders.authentication.AuthenticationRequest;
import com.algorhythm.booking_backend.dataproviders.authentication.AuthenticationResponse;
import com.algorhythm.booking_backend.dataproviders.User.NewUserDto;
import com.algorhythm.booking_backend.dataproviders.User.UserDto;
import com.algorhythm.booking_backend.dataproviders.authentication.DeauthenticationRequest;
import com.algorhythm.booking_backend.services.interfaces.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(
        name = "User",
        description = "All APIs of Users"
)
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "Authenticate and receive a token")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request){
            AuthenticationResponse response = userService.authenticate(request);
            return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    @Operation(summary = "Deauthenticate")
    public ResponseEntity<Void> deauthenticate(@Valid @RequestBody DeauthenticationRequest request){
        if (userService.deauthenticate(request))
            return new ResponseEntity<>(HttpStatus.OK);

        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    @Operation(summary = "Get all users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> allUsers = userService.findAll();
        return ResponseEntity.ok(allUsers);
    }

    @GetMapping("/user")
    @Operation(summary = "Get user by id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> getUserById(@RequestParam Integer userId){
        UserDto user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> createUser(@RequestBody NewUserDto newUser){

        UserDto user = userService.addUser(newUser);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Remove a user by its id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> removeUser(@RequestParam Integer userId){
            userService.removeUser(userId);
            return ResponseEntity.ok(null);
    }
}
