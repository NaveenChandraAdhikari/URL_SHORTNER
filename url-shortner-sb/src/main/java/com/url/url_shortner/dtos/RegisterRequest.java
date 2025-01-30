package com.url.url_shortner.dtos;


import lombok.Data;

import java.util.Set;
@Data
//define the structire of the request for registeration
public class RegisterRequest  {

    private String username;
    private String email;
    private Set<String> role;
    private String password;

}
