package com.project.grocery.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import com.project.grocery.Jwt.JwtHelper;
import com.project.grocery.Jwt.JwtRequest;
import com.project.grocery.Jwt.JwtResponse;


@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    // Logger for JwtAuthenticationController class
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationController.class);

    // Endpoint to authenticate user and generate JWT token
    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        // Log authentication request
        logger.info("Received authentication request for user: {}", authenticationRequest.getUsername());
        
        // Authenticate the user
        try {
            authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        } catch (Exception e) {
            // Log authentication failure
            logger.error("Authentication failed for user: {}", authenticationRequest.getUsername());
            // Return response with invalid credentials message
            return ResponseEntity.badRequest().body(new JwtResponse(authenticationRequest.getUsername(), "INVALID_CREDENTIALS"));
        }
    
        // Load user details
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        
        // Generate JWT token
        final String token = jwtHelper.generateToken(userDetails);
        
        // Log successful authentication
        logger.info("User {} authenticated successfully", authenticationRequest.getUsername());
    
        // Return JWT token in response
        return ResponseEntity.ok(new JwtResponse(userDetails.getUsername(), token));
    }

    private void authenticate(String username, String password) throws Exception {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    }
}