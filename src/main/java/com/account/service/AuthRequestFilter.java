package com.account.service;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;

@Component
@Slf4j
public class AuthRequestFilter extends OncePerRequestFilter {

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	private AuthTokenUtil authTokenUtil;

	@Autowired
	private TokenBlacklist tokenBlacklist;

	private static final Logger logger = LoggerFactory.getLogger(AuthRequestFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		logger.info("request URI ::" + request.getRequestURI());
		if (SecurityContextHolder.getContext().getAuthentication() == null) {
			String username = null;
			String jwtToken = authTokenUtil.extractTokenFromRequest(request);
			try {
				if (!tokenBlacklist.isBlacklisted(jwtToken)) {
					username = authTokenUtil.extractUsername(jwtToken);
					logger.info("username" + username);
				} else
					throw new ExpiredJwtException(null, null, jwtToken);
			} catch (IllegalArgumentException e) {
				logger.error("Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				logger.error("JWT Token has expired");
			}

			if (username != null) {
				UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(username);
				if (authTokenUtil.validateToken(jwtToken, userDetails)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
		}
		chain.doFilter(request, response);
	}

}
