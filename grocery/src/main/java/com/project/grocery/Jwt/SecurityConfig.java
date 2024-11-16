package com.project.grocery.Jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    // Autowire JwtAuthenticationEntryPoint and JwtAuthenticationFilter
    @Autowired
    private JwtAuthenticationEntryPoint point;
    @Autowired
    private JwtAuthenticationFilter filter;

    @SuppressWarnings("deprecation")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configure HttpSecurity
        http.csrf(csrf -> csrf.disable()) // Disable CSRF protection
                .authorizeRequests(requests -> requests
                        .requestMatchers("/authenticate").permitAll() // Permit access to /authenticate endpoint
                        .anyRequest().authenticated()) // Require authentication for any other request
                .exceptionHandling(ex -> ex.authenticationEntryPoint(point)) // Handle authentication exceptions
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Set session management to stateless
        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class); // Add JwtAuthenticationFilter before UsernamePasswordAuthenticationFilter
        return http.build();
    }

}
