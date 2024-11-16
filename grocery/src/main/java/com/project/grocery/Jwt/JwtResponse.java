package com.project.grocery.Jwt;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class JwtResponse {

    private String username;
    private String jwtToken;
   
    public JwtResponse(String username, String jwtToken) {
        this.username = username;
        this.jwtToken = jwtToken;
    }
}
