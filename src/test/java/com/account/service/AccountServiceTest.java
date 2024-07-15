package com.account.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.account.constant.ErrorConstants;
import com.account.entity.Account;
import com.account.exception.ApiErrorException;
import com.account.model.AccountDTO;
import com.account.repo.AccountRepository;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    // Initialize mock data
    private AccountDTO sampleAccountDTO;
    private Account sampleAccount;
    private List<Account> sampleAccountList;

    @BeforeEach
    void setUp() {
        // Initialize sample data before each test
        sampleAccountDTO = new AccountDTO.Builder()
                .setAccountName("Test Account")
                .setAccountNumber("ACC123456")
                .setBalance(BigDecimal.valueOf(100.0))
                .setCreatedDate(LocalDateTime.now())
                .setUpdatedDate(LocalDateTime.now())
                .build();

        sampleAccount = new Account.Builder()
                .setAccountName("Test Account")
                .setAccountNumber("ACC123456")
                .setBalance(BigDecimal.valueOf(100.0))
                .setCreatedDate(LocalDateTime.now())
                .setUpdatedDate(LocalDateTime.now())
                .build();

        sampleAccountList = new ArrayList<>();
        sampleAccountList.add(sampleAccount);
    }

    @Test
    void testSaveAccount() {
        when(accountRepository.save(any())).thenReturn(sampleAccount);
        AccountDTO result = accountService.saveAccount(sampleAccountDTO);
        assertEquals(sampleAccountDTO.getAccountName(), result.getAccountName());
    }

    @Test
    void testUpdateAccount() {
        Long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(sampleAccount));
        when(accountRepository.save(any())).thenReturn(sampleAccount);
        AccountDTO updatedAccount = accountService.updateAccount(sampleAccountDTO, accountId);
        assertEquals(sampleAccountDTO.getAccountName(), updatedAccount.getAccountName());
    }

    @Test
    void testUpdateNonExistingAccount() {
        Long nonExistingId = 999L;
        when(accountRepository.findById(nonExistingId)).thenReturn(Optional.empty());
        ApiErrorException exception = assertThrows(ApiErrorException.class, () -> {
            accountService.updateAccount(sampleAccountDTO, nonExistingId);
        });
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(ErrorConstants.ACCOUNT_NOT_UPDATE_CODE, exception.getErrorCode());
    }

    @Test
    void testGetAccountByAccountNumber() {
        String accountNumber = "ACC123456";
        when(accountRepository.findAllByAccountNumber(accountNumber)).thenReturn(Optional.of(sampleAccountList));
        List<Account> result = accountService.getAccountByAccountNumber(accountNumber);
        assertFalse(result.isEmpty());
        assertEquals(sampleAccountList.size(), result.size());
    }

    @Test
    void testGetNonExistingAccountByAccountNumber() {
        String nonExistingAccountNumber = "NONEXIST123";
        when(accountRepository.findAllByAccountNumber(nonExistingAccountNumber)).thenReturn(Optional.empty());

        ApiErrorException exception = assertThrows(ApiErrorException.class, () -> {
            accountService.getAccountByAccountNumber(nonExistingAccountNumber);
        });
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(ErrorConstants.ACCOUNT_NOT_FOUND_ACC_NUMBER_CODE, exception.getErrorCode());
    }

    @Test
    void testGetAccountByAccountName() {
        String accountName = "Test Account";
        when(accountRepository.findAllByAccountName(accountName)).thenReturn(Optional.of(sampleAccountList));
        List<Account> result = accountService.getAccountByAccountName(accountName);
        assertFalse(result.isEmpty());
        assertEquals(sampleAccountList.size(), result.size());
    }

    @Test
    @DisplayName("Attempt to get non-existing account by account name")
    void testGetNonExistingAccountByAccountName() {
        String nonExistingAccountName = "Nonexistent Account";
        when(accountRepository.findAllByAccountName(nonExistingAccountName)).thenReturn(Optional.empty());
        ApiErrorException exception = assertThrows(ApiErrorException.class, () -> {
            accountService.getAccountByAccountName(nonExistingAccountName);
        });
        assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode());
        assertEquals(ErrorConstants.ACCOUNT_NAME_NOT_FOUND_ACC_NAME_CODE, exception.getErrorCode());
    }

}
