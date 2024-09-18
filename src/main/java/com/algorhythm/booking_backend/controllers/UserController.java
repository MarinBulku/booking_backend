package com.algorhythm.booking_backend.controllers;

import com.algorhythm.booking_backend.dataproviders.dtos.authentication.AuthenticationRequest;
import com.algorhythm.booking_backend.dataproviders.dtos.authentication.AuthenticationResponse;
import com.algorhythm.booking_backend.dataproviders.dtos.user.NewUserDto;
import com.algorhythm.booking_backend.dataproviders.dtos.user.UserDto;
import com.algorhythm.booking_backend.dataproviders.dtos.authentication.DeauthenticationRequest;
import com.algorhythm.booking_backend.dataproviders.services.interfaces.UserService;
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
    /*
    * User Controller endpoints
    * userService - to get User Services
    * */

    /*
    * authenticate(AuthenticationRequest request)
    * request - The request to authenticate
    *
    * Has no authorization.
    * If successful, returns an AuthenticationResponse
    * Else returns an exception handled by global exception handler
    * */
    @PostMapping("/login")
    @Operation(summary = "Authenticate and receive a token")
    public ResponseEntity<AuthenticationResponse> authenticate(@Valid @RequestBody AuthenticationRequest request){
            AuthenticationResponse response = userService.authenticate(request);
            return ResponseEntity.ok(response);
    }
    /*
    * deauthenticate(DeauthenticationRequest request)
    * request - The request to deauthenticate
    *
    * Must be an admin or a user to access this endpoint
    * If successful, a void response is returned with HTTPStatus = OK
    * else it is returned a void response with HTTPStatus = BAD_REQUEST
    * */
    @PostMapping("/logout")
    @Operation(summary = "Deauthenticate")
    public ResponseEntity<Void> deauthenticate(@Valid @RequestBody DeauthenticationRequest request){
        if (userService.deauthenticate(request))
            return new ResponseEntity<>(HttpStatus.OK);

        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    /*
    * getAllUsers() - No parameters
    *
    * Must be an admin to access this endpoint
    * It is returned a list of all users in UserDto dto
    * */
    @GetMapping
    @Operation(summary = "Get all users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDto>> getAllUsers(){
        List<UserDto> allUsers = userService.findAll();
        return ResponseEntity.ok(allUsers);
    }

    /*
     * getUserById(Integer userId)
     * userId - ID of user to be searched
     *
     * It returns the user as UserDto object if found,
     * Else BAD_REQUEST with a message is returned
     * */
    @GetMapping("/user")
    @Operation(summary = "Get user by id")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserDto> getUserById(@RequestParam Integer userId){
        UserDto user = userService.findById(userId);
        return ResponseEntity.ok(user);
    }

    /*
     * createUser(NewUserDto newUser)
     * newUser - Request of new user to be created
     *
     * Must be an admin to access this endpoint
     * It returns the user as UserDto object if created successfully,
     * Else BAD_REQUEST with a message is returned
     * */
    @PostMapping("/create")
    @Operation(summary = "Create a new user")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDto> createUser(@RequestBody NewUserDto newUser){

        UserDto user = userService.addUser(newUser);
        return ResponseEntity.ok(user);
    }

    /*
     * removeUser(Integer userId)
     * userId - ID of user to be removed
     *
     * Must be an admin to access this endpoint
     * It returns empty response if removed successfully,
     * Else BAD_REQUEST with a message is returned
     * */
    @DeleteMapping("/delete")
    @Operation(summary = "Remove a user by its id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> removeUser(@RequestParam Integer userId){
            userService.removeUser(userId);
            return ResponseEntity.ok("Deleted user successfully");
    }
}
