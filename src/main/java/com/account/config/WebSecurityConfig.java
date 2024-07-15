package com.account.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.account.service.filter.AuthRequestFilter;
import com.account.service.filter.JwtAuthenticationEntryPoint;
import com.account.service.impl.UserDetailsServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    private final AuthRequestFilter authRequestFilter;

    private final UserDetailsServiceImpl userDetailsServiceImp;

    public WebSecurityConfig(JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint, AuthRequestFilter authRequestFilter, UserDetailsServiceImpl userDetailsServiceImp) {
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.authRequestFilter = authRequestFilter;
        this.userDetailsServiceImp = userDetailsServiceImp;
    }

    @Bean
    SecurityFilterChain configure(HttpSecurity http) throws Exception {
        log.info("configure () called");
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        req -> req.requestMatchers("/api/login", "/swagger-ui/**", "/swagger-ui.html", "/swagger-ui/index.html/**",
                                "/v3/api-docs/**", "/webjars/**", "/public/**").permitAll().anyRequest().authenticated())
                .httpBasic(withDefaults()).userDetailsService(userDetailsServiceImp)
                .exceptionHandling(entrypoint -> entrypoint.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(authRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

}
