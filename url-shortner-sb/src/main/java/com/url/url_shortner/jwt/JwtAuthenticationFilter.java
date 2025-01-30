package com.url.url_shortner.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//filter that make sure every request has jwt tokem on it..for evert rewuest this piece of code excuted

//this filter does the job of validating the jwt token in the req header and also responsible for loading the userinfo ,,,
//but spring sec does not add this custom chain automatically ,,we need to tell spring sec that you need to execute this filter and wjen we need to configure this cistom sec config

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtTokenProvider;

@Autowired
private UserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

//        add operation that help you authenticate the request

        try{
            //to aunthenticate evey req we have series of steps
//           1. get jwt from header..this make helps that user is valid or not
            String jwt=jwtTokenProvider.getJwtFromHeader(request);
            if(jwt!=null && jwtTokenProvider.validateToken(jwt) ){
                String username=jwtTokenProvider.getUsernameFromJwtToken(jwt);
                //logic to load user specific data using userdetailsservice
                UserDetails userDetails=userDetailsService.loadUserByUsername(username);
                //how spring security know how to load the user information the answer is to use userdetailserviceimpl and override the laoduserbyusrname and define your logic
                if(userDetails!=null){
                    UsernamePasswordAuthenticationToken authentication=new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
            //2. validate the token
            //3. if valid get user details
            // -- get user name --> load user  --> set auth context

        }
        catch (IllegalArgumentException e){
            e.printStackTrace();
        }
        catch (ExpiredJwtException e){

            e.printStackTrace();
        }
        catch (MalformedJwtException e){
            e.printStackTrace();
        }
        catch (Exception e){
e.printStackTrace();
        }
//        to not break the chain
        filterChain.doFilter(request,response);
    }
}

/*
The reason we use UserDetailsService to load user information even after extracting the username from the JWT token is to retrieve complete user-specific details and roles/authorities. Here’s a simplified explanation:
Steps and Reasons:

    Extract Username from JWT:
        The JWT contains basic information (like username or user ID) in its claims.
        This is enough to identify the user but doesn't provide complete details like roles/authorities or other required user-specific data.

    Retrieve Full User Details:
        The UserDetailsService implementation (loadUserByUsername) is used to fetch the user from the database or another user store.
        This step ensures we have:
            User roles/authorities: Necessary for security checks (e.g., hasRole).
            Additional details: Locked account status, enabled status, etc.

    Set Authentication Context:
        Once we retrieve the UserDetails, we create an authentication object (UsernamePasswordAuthenticationToken) and set it in the SecurityContextHolder.
        This ensures Spring Security knows who the current user is and what permissions they have.

Why Not Just Use the JWT Claims?

    The JWT might not store all necessary details (e.g., roles, account status).
    User roles/permissions may have changed since the JWT was issued.
    Validating and loading fresh data from a trusted source (database) ensures better security.

Example:

If a user's role changes from ROLE_USER to ROLE_ADMIN, and their token was issued before the change, relying solely on the JWT would lead to outdated permissions. By using UserDetailsService, you fetch the current state of the user from the database.

In short:

    JWT: Provides a lightweight, fast mechanism to identify the user (username/ID).
    UserDetailsService: Ensures accurate and up-to-date user details and permissions are used.
 */