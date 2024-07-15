package com.account.controller;

import com.account.entity.Account;
import com.account.model.AccountDTO;
import com.account.service.AccountService;
import com.account.service.filter.AuthTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountControllerTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountController accountController;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AuthTokenUtil authTokenUtil;

    private AccountDTO inputAccountDTO;
    private AccountDTO accountDTO;
    ObjectMapper objectMapper;
    private String bearerToken;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialize test data
        inputAccountDTO = new AccountDTO.Builder()
                .setAccountName("Test Account")
                .setAccountNumber("1234567890")
                .setBalance(BigDecimal.valueOf(1000.0))
                .setId(1L)
                .build();

        accountDTO = new AccountDTO.Builder()
                .setId(1L)
                .setAccountName("Test Account")
                .setAccountNumber("1234567890")
                .setBalance(BigDecimal.valueOf(1000.0))
                .build();

        objectMapper = new ObjectMapper();
        bearerToken = authTokenUtil.generateToken(new User("user", "password", new ArrayList<>()));
    }


    @Test
    @WithMockUser(username = "user", password = "password")
    public void testCreateAccount() throws Exception {
        when(accountService.saveAccount(any(AccountDTO.class))).thenReturn(accountDTO);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
                        .header("Authorization", "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputAccountDTO)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value(accountDTO.getAccountNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountName").value(accountDTO.getAccountName()));
    }

    @Test
    public void testUpdateAccount() throws Exception {
        when(accountService.updateAccount(any(AccountDTO.class), any(Long.class))).thenReturn(accountDTO);
        mockMvc.perform(MockMvcRequestBuilders.put("/api/accounts/{id}", inputAccountDTO.getId())
                        .header("Authorization", "Bearer " + bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputAccountDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value(accountDTO.getAccountNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(accountDTO.getId()));
    }

    @Test
    public void testGetAccounts() throws Exception {
        int page = 0;
        int size = 2;

        List<AccountDTO> li = Collections.emptyList();
        when(accountService.getAllAccounts(PageRequest.of(page, size)))
                .thenReturn(li);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts")
                        .header("Authorization", "Bearer " + bearerToken)
                        .param("pageSize", String.valueOf(size))
                        .param("page", String.valueOf(page)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetAccountsByAccountNumber() throws Exception {
        String accountNumber = "1234567890";
        Account account = new Account.Builder().setAccountName("Test Account")
                .setAccountNumber("1234567890")
                .setBalance(BigDecimal.valueOf(1022.1))
                .setId(1L)
                .build();
        List<Account> accounts = new ArrayList<>();
        accounts.add(account);
        when(accountService.getAccountByAccountNumber(accountNumber))
                .thenReturn(accounts);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts/account")
                        .header("Authorization", "Bearer " + bearerToken)
                        .param("accountNumber", accountNumber))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountNumber").value(inputAccountDTO.getAccountNumber()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountName").value(inputAccountDTO.getAccountName()));
    }
}
