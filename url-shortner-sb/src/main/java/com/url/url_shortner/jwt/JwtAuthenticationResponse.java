package com.url.url_shortner.jwt;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
//this is the dto class that basically represent the authentication response
public class JwtAuthenticationResponse {



    private String token;


}