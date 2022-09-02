package com.jwt.controller;

import com.helper.JwtUtil;
import com.model.JwtRequest;
import com.model.JwtResponse;
import com.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
   @Autowired
    private JwtUtil jwtUtil;


    @RequestMapping(value = "/token",method = RequestMethod.POST)
    public ResponseEntity<?> genrateToken(@RequestBody JwtRequest jwtRequest) throws Exception
    {
             System.out.println(jwtRequest);
              try
              {
       this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getUsername(),jwtRequest.getPassword()));
              }
              catch (UsernameNotFoundException e)
              {
                  e.printStackTrace();
                  throw new Exception("Bad Credentails");
              }
              catch (BadCredentialsException b)
              {
                  b.printStackTrace();
                  throw new Exception("Bad Credentails");
              }
        UserDetails userDetails= this.customUserDetailsService.loadUserByUsername(jwtRequest.getUsername());
             String token= this.jwtUtil.generateToken(userDetails);
             System.out.println("JWT Token"+token);

             return  ResponseEntity.ok(new JwtResponse(token));
    }
}
