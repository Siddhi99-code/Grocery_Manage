
package com.project.grocery.Jwt;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Get the token from the Authorization header
        String token = request.getHeader("Authorization");
        String username = null;

        if (token != null) {
            try {
                // Extract the username from the JWT token
                username = this.jwtHelper.getUsernameFromToken(token);
            } catch (IllegalArgumentException e) {
                // Handle illegal argument exception
                logger.info("Illegal Argument while fetching the username !!");
            } catch (ExpiredJwtException e) {
                // Handle expired JWT token exception
                logger.info("Given JWT token is expired !!");
            } catch (MalformedJwtException e) {
                // Handle malformed JWT token exception
                logger.info("Some changes have been made to the token !! Invalid Token");
            } catch (Exception e) {
                // Handle generic exception
                logger.info("An error occurred while fetching the username from the token.");
            }
        } else {
            // Log a message for a missing token
            logger.info("Token is missing in the Authorization header !!");
        }

        // If username is obtained and no authentication is set in the SecurityContextHolder
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Fetch user details from the userDetailsService
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // Validate the JWT token
            Boolean validateToken = this.jwtHelper.validateToken(token, userDetails);
            if (validateToken) {
                // If the token is valid, set the authentication in the SecurityContextHolder
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                // Log a message if token validation fails
                logger.info("Token validation failed !!");
            }
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
}

