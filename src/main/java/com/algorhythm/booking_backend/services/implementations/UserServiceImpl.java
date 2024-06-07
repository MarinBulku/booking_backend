package com.algorhythm.booking_backend.services.implementations;

import com.algorhythm.booking_backend.entities.Role;
import com.algorhythm.booking_backend.entities.User;
import com.algorhythm.booking_backend.exceptions.EntityNotFoundException;
import com.algorhythm.booking_backend.repositories.RoleRepository;
import com.algorhythm.booking_backend.repositories.UserRepository;
import com.algorhythm.booking_backend.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    public final UserRepository userRepository;
    public final RoleRepository roleRepository;
    @Override
    public List<User> findAll() {

        List<User> allUsers = userRepository.findAll();

        if (allUsers.isEmpty()) return new ArrayList<User>();

        return allUsers;
    }

    @Override
    public User findById(Integer id) {

        Optional<User> foundUser  = userRepository.findById(id);

        if (foundUser.isPresent()) return foundUser.get();

        else throw new EntityNotFoundException("No user with this id: " + id);

    }

    @Override
    public boolean existsById(Integer id) {
        return userRepository.existsById(id);
    }

    @Override
    public User addCustomer(String name, String email, String password, String number, Integer roleId, String Address) {

            Optional<Role> optional = roleRepository.findById(roleId);
            if (optional.isEmpty()) throw new EntityNotFoundException("No role with this id: " +roleId);

        User newUser = User.builder()
                .fullName(name)
                .email(email)
                .password(password)
                .phoneNumber(number)
                .role(optional.get())
                .address(Address)
                .build();
        return userRepository.save(newUser);
    }

    @Override
    public void removeCustomer(Integer idOfUserToBeRemoved) {
        if (!userRepository.existsById(idOfUserToBeRemoved)) throw new EntityNotFoundException("No user with this id: "+ idOfUserToBeRemoved);
        roleRepository.deleteById(idOfUserToBeRemoved);
    }
}
