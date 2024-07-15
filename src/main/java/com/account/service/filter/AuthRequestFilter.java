package com.account.service.filter;

import java.io.IOException;

import com.account.service.TokenBlacklist;
import com.account.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
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

    private final UserDetailsServiceImpl userDetailsServiceImpl;

    private final AuthTokenUtil authTokenUtil;

    private final TokenBlacklist tokenBlacklist;


    public AuthRequestFilter(UserDetailsServiceImpl userDetailsServiceImpl, AuthTokenUtil authTokenUtil, TokenBlacklist tokenBlacklist) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.authTokenUtil = authTokenUtil;
        this.tokenBlacklist = tokenBlacklist;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        log.info("request URI :: {}", request.getRequestURI());
        String jwtToken = authTokenUtil.extractTokenFromRequest(request);
        if (jwtToken != null) {
            if (!tokenBlacklist.isBlacklisted(jwtToken)) {
                String username = authTokenUtil.extractUsername(jwtToken);
                log.info("username :: {} ", username);
                UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(username);
                if (authTokenUtil.validateToken(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            } else
                throw new ExpiredJwtException(null, null, jwtToken);
        }
        chain.doFilter(request, response);
    }
}
