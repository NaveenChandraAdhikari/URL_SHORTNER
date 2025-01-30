package com.url.url_shortner.controller;

import com.url.url_shortner.jwt.JwtUtils;
import com.url.url_shortner.models.User;
import com.url.url_shortner.repository.UserRepository;
import com.url.url_shortner.service.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Collections;


//@Controller
@RequestMapping("/api/auth")
public class OAuth2Controller {



    @Autowired
    UserRepository userRepository;

    private final JwtUtils jwtUtils;

    @Value("${frontend.url}")
    private String frontendUrl;

    public OAuth2Controller(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }


    @GetMapping("/oauth-success")
    public void oauthSuccess(OAuth2User principal, HttpServletResponse response) throws IOException {
        // Extract user details from the OAuth2User object
        String username = principal.getAttribute("login"); // GitHub username
        String email = principal.getAttribute("email");

        // Check if the user already exists in your database
        User user = userRepository.findByUsername(username)
                .orElseGet(() -> {
                    // If the user doesn't exist, create a new user
                    User newUser = new User();
                    newUser.setUsername(username);
                    newUser.setEmail(email);
                    newUser.setPassword(""); // OAuth users don't need a password
//                    newUser.setRole("USER_ROLE"); // Default role for OAuth users
                    return userRepository.save(newUser);
                });

        // Create a UserDetailsImpl object using the build method
        UserDetailsImpl userDetails = UserDetailsImpl.build(user);

        // Generate a JWT for the user
        String jwtToken = jwtUtils.generateToken(userDetails);

        // Redirect to the frontend dashboard
        String redirectUrl = frontendUrl + "/dashboard";
        response.sendRedirect(redirectUrl);
    }

    @GetMapping("/oauth-failure")
    public void oauthFailure(HttpServletResponse response) throws IOException {
        // Redirect to the frontend login page with an error message
        String redirectUrl = frontendUrl + "/login?error=oauth_failed";
        response.sendRedirect(redirectUrl);
    }
}
