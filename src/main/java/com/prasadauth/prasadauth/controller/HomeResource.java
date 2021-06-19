package com.prasadauth.prasadauth.controller;

import com.prasadauth.prasadauth.models.JwtResponse;
import com.prasadauth.prasadauth.models.UserRequest;
import com.prasadauth.prasadauth.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.web.servlet.oauth2.login.OAuth2LoginSecurityMarker;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeResource
{
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String hello()
    {
        return "<h1>Hello World</h1>";
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createToken(@RequestBody UserRequest userRequest) throws Exception {
        try
        {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userRequest.getUsername(),userRequest.getPassword())
            );
        }
        catch (BadCredentialsException e)
        {
             throw  new Exception("Incorrect username or password");
        }

        final UserDetails userDetails= userDetailsService.loadUserByUsername(userRequest.getUsername());

        final String jwt=jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(jwt));

    }
}
