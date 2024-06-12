package com.algorhythm.booking_backend.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(request ->
                                        new CorsConfiguration().applyPermitDefaultValues()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                            auth.requestMatchers("/swagger-ui/**").permitAll();
                            auth.requestMatchers("/swagger-ui.html").permitAll();
                            auth.requestMatchers("/api-docs/**").permitAll();
                            auth.requestMatchers("/users/login").permitAll();
                            auth.requestMatchers("/users/logout").permitAll();
                            auth.anyRequest().authenticated();
                        }
                )
                .sessionManagement(session ->
                        session
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
