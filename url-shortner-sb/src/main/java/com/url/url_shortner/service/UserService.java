package com.url.url_shortner.service;

import com.url.url_shortner.dtos.LoginRequest;
import com.url.url_shortner.jwt.JwtAuthenticationResponse;
import com.url.url_shortner.jwt.JwtUtils;
import com.url.url_shortner.models.User;
import com.url.url_shortner.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class UserService {


    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;
private AuthenticationManager authenticationManager;

private JwtUtils jwtUtils;
    public User registerUser(User user) {
        // Check if the username already exists
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists!");
        }

        // Encode the password and save the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


    public JwtAuthenticationResponse authenticateLoginUser(LoginRequest loginRequest){

        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword())


        );
//        if the userame and password are valid then we will have authentication object
        SecurityContextHolder.getContext().setAuthentication(authentication);
//        then get the instance of userdetails
        UserDetailsImpl userDetails= (UserDetailsImpl) authentication.getPrincipal();
        String jwt=jwtUtils.generateToken(userDetails);
        return new JwtAuthenticationResponse(jwt);
    }

//    to retrieve the usr info based on the username
    public User findByUsername(String name) {

        return userRepository.findByUsername(name).orElseThrow(
                ()->new UsernameNotFoundException("User not found with usernamee::"+ name )
        );
    }
}
