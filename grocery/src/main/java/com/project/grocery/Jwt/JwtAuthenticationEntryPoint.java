// package com.project.grocery.Jwt;

// import java.io.IOException;
// import java.io.PrintWriter;

// import org.springframework.security.core.AuthenticationException;
// import org.springframework.security.web.AuthenticationEntryPoint;
// import org.springframework.stereotype.Component;

// import jakarta.servlet.ServletException;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpServletResponse;

// @Component
// public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

//     // This method is called when a user tries to access a secured resource without proper authentication
//     @Override
//     public void commence(HttpServletRequest request, HttpServletResponse response,
//             AuthenticationException authException) throws IOException, ServletException {
//         // Set the response status to 401 Unauthorized
//         response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//         // Write a message indicating access denied along with any relevant authentication exception message
//         PrintWriter writer = response.getWriter();
//         writer.println("Access Denied !! " + authException.getMessage());
//     }
    
// }
package com.project.grocery.Jwt;

import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    // This method is called when a user tries to access a secured resource without proper authentication
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        // Set the response status to 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // Set the content type to application/json
        response.setContentType("application/json");
        // Create a JSON string with the error details
        String jsonResponse = String.format("{\"error_code\": \"%d\", \"message\": \"%s\"}", 
                                             HttpServletResponse.SC_UNAUTHORIZED, 
                                             "Access Denied !! " + authException.getMessage());
        // Write the JSON string to the response
        PrintWriter writer = response.getWriter();
        writer.write(jsonResponse);
        writer.flush();
    }
}
