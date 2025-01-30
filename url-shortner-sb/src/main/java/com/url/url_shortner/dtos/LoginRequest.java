package com.url.url_shortner.dtos;


import lombok.Data;

import java.util.Set;

@Data
//define the structire of the request for registeration
public class LoginRequest {

    private String username;

    private String password;

}
