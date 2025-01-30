package com.url.url_shortner.service;

import com.url.url_shortner.models.User;
import com.url.url_shortner.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
//this is how user suppposed to be loaded and authenticated and also we need to make daoauthentication provider that accepts the instance of user details service impl so that it is aware that hey load the user details this way
@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    UserRepository userRepository;
//    userrepo will do the job for interacting witht the user model
    @Override
    @Transactional
//    the user details here the userdetails of spring security
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user =userRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("User not found with username: "+username));
//load from our database and convert the user into object type that spring sec understands.....actually it laods thus data inside the filter then you know what steps are taken into dofilerinterval
        return UserDetailsImpl.build(user) ;
    }
}

/*
We override the loadUserByUsername method in UserDetailsService to define how to fetch user details (e.g., username, password, roles) from our specific data source (e.g., database). Here's why:
Key Reasons:

    Custom Data Source:
        Spring Security doesn’t know how to get user information from your database or data store.
        By overriding loadUserByUsername, you provide the logic to fetch the user details (e.g., from a database, API, etc.).

    Custom User Logic:
        You can specify what information is required for authentication, such as roles, status, or any other fields unique to your application.

    Enable Spring Security:
        Spring Security uses loadUserByUsername to fetch the user, verify their credentials, and authenticate them.
        Without this, Spring Security won't know how to validate users in your system.

Simple Example:

Suppose your app has a User table in a database. To fetch user details, you'd:

    Query the database for the username.
    Convert the result into a UserDetails object (with username, password, and roles).
 */