package com.account.controller;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.account.service.AuthTokenUtil;
import com.account.service.TokenBlacklist;
import com.account.service.UserDetailsServiceImpl;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping(value = "/api")
public class AuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private AuthTokenUtil authTokenUtil;

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private TokenBlacklist tokenBlacklist;

	private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

	@PostMapping(value = "/login")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthRequest authenticationRequest)
			throws Exception {

		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String token = authTokenUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthResponse(token));

	}

	@SecurityRequirement(name = "Authorization")
	@PostMapping(value = "/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) throws ServletException {
		logger.info("/logout called ");
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
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}
}
