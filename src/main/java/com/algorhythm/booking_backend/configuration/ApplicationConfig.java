package com.algorhythm.booking_backend.configuration;

import com.algorhythm.booking_backend.core.entities.User;
import com.algorhythm.booking_backend.datasources.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class ApplicationConfig {

    private final UserRepository userRepository;

    /*
     * userDetailsService()
     *
     * Creates and returns a UserDetailsService bean
     * The UserDetailsService loads user data based on the username which can be an email or phone number
     *
     * If the user is not found by email or phone number, a UsernameNotFoundException is thrown
     *
     * It returns UserDetailsService
     */
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> {
            Optional<User> user = userRepository.findByEmail(username);
            if (user.isEmpty()) {
                user = userRepository.findByPhoneNumber(username);
            }
            return user.orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        };
    }

    /*
     * authenticationProvider()
     *
     * Creates and returns an AuthenticationProvider bean
     * The AuthenticationProvider authenticates users using the UserDetailsService and a password encoder
     *
     * It returns AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /*
     * authenticationManager(AuthenticationConfiguration authenticationConfiguration)
     *
     * Creates and returns an AuthenticationManager bean
     * The AuthenticationManager manages authentication by delegating to the provided AuthenticationConfiguration
     *
     * authenticationConfiguration - the authentication configuration to use
     * It returns AuthenticationManager
     * Throws Exception if an error occurs when obtaining the authentication manager
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /*
     * passwordEncoder()
     *
     * Creates and returns a PasswordEncoder bean
     * The PasswordEncoder encodes passwords using the BCrypt hashing algorithm with a strength of 10
     *
     * It returns PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
