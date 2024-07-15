package com.account.controller;

import com.account.model.AuthRequest;
import com.account.service.AuthTokenUtil;
import com.account.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserDetailsServiceImpl userDetailsService;
    @InjectMocks
    private AuthenticationController authenticationController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthTokenUtil authTokenUtil;

    @Test
    public void testCreateAuthenticationToken() throws Exception {
        String username = "user";
        String password = "password";
        AuthRequest authRequest = new AuthRequest(username, password);
        UserDetails userDetails = new User(username,password,new ArrayList<>());
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(userDetailsService.loadUserByUsername(username)).thenReturn(userDetails);
        ObjectMapper objectMapper = new ObjectMapper();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testLogout() throws Exception {
        String bearerToken = authTokenUtil.generateToken(new User("user", "password", new ArrayList<>()));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer "+bearerToken))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("User logged out successfully"));
    }
}
