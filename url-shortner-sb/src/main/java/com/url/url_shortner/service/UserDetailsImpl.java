package com.url.url_shortner.service;


import com.url.url_shortner.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


//now sprinf security aware of that user is the user i want to deal with or aware of and use usedtails ...userdetailIMpl is the bridge between user and userDetails
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsImpl  implements UserDetails {


    private static final long serialVersionUID=1L;

    private Long id;
    private String username;
    private String email;

    private String password;

    private Collection<? extends GrantedAuthority> authorities;


//    convert user object from our database to userdetails implementation object for spring security because spring security going to make use of this

public static UserDetailsImpl build(User user){
    GrantedAuthority authority=new SimpleGrantedAuthority(user.getRole());
    return new UserDetailsImpl(

            user.getId(),
            user.getUsername(),
            user.getEmail(),
            user.getPassword(),
            Collections.singletonList(authority)
    );
}



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
