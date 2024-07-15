package com.account.controller;

import java.util.Objects;

import com.account.exception.ApiErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.account.model.AuthRequest;
import com.account.model.AuthResponse;
import com.account.service.filter.AuthTokenUtil;
import com.account.service.TokenBlacklist;
import com.account.service.impl.UserDetailsServiceImpl;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/api")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;

    private final AuthTokenUtil authTokenUtil;

    private final UserDetailsServiceImpl userDetailsService;

    private final TokenBlacklist tokenBlacklist;

    public AuthenticationController(AuthenticationManager authenticationManager, AuthTokenUtil authTokenUtil, UserDetailsServiceImpl userDetailsService, TokenBlacklist tokenBlacklist) {
        this.authenticationManager = authenticationManager;
        this.authTokenUtil = authTokenUtil;
        this.userDetailsService = userDetailsService;
        this.tokenBlacklist = tokenBlacklist;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<AuthResponse> createAuthenticationToken(@RequestBody AuthRequest authenticationRequest)
            throws Exception {
        log.info("AuthenticationController :: createAuthenticationToken()");
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = authTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(token));

    }

    @SecurityRequirement(name = "Authorization")
    @PostMapping(value = "/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) throws ServletException {
        log.info("/logout called ");
        // Using blacklist token approach
        String token = authTokenUtil.extractTokenFromRequest(request);
        tokenBlacklist.addToBlacklist(token);
        return ResponseEntity.ok("User logged out successfully");
    }

    private void authenticate(String username, String password) throws Exception {
        Objects.requireNonNull(username);
        Objects.requireNonNull(password);
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            log.info("authenticated user");
        } catch (DisabledException e) {
            throw new ApiErrorException(401, "USER_DISABLED", "USER_DISABLED");
        } catch (BadCredentialsException e) {
            throw new ApiErrorException(401, "INVALID_CREDENTIALS", "INVALID_CREDENTIALS");
        }
    }
}
