package com.account.service;

import com.account.entity.Account;
import com.account.model.AccountDTO;
import com.account.repo.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private AccountDTO testAccountDTO;
    private Account testAccount;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Initialize test data
        testAccountDTO = new AccountDTO.Builder()
                .setAccountName("Test Account")
                .setAccountNumber("1234567890")
                .setBalance(BigDecimal.valueOf(1000.0))
                .setCreatedDate(LocalDateTime.now())
                .setUpdatedDate(LocalDateTime.now())
                .build();

        testAccount = new Account.Builder()
                .setAccountName("Test Account")
                .setAccountNumber("1234567890")
                .setBalance(BigDecimal.valueOf(1000.0))
                .setCreatedDate(LocalDateTime.now())
                .setUpdatedDate(LocalDateTime.now())
                .build();
    }

    @Test
    public void testSaveAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);
        AccountDTO savedAccountDTO = accountService.saveAccount(testAccountDTO);
        assertNotNull(savedAccountDTO);
        assertEquals(testAccountDTO.getAccountName(), savedAccountDTO.getAccountName());
        assertEquals(testAccountDTO.getAccountNumber(), savedAccountDTO.getAccountNumber());
        assertEquals(testAccountDTO.getBalance(), savedAccountDTO.getBalance());
        verify(accountRepository, times(1)).save(any(Account.class));
    }
}