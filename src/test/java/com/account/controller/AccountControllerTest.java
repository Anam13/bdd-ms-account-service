package com.account.controller;
import com.account.entity.Account;
import com.account.model.AccountDTO;
import com.account.service.AccountService;
import com.account.service.AuthTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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


	@Test
	@WithMockUser(username = "user", password = "password")
	public void testCreateAccount() throws Exception {

		String bearerToken = authTokenUtil.generateToken(new User("user", "password", new ArrayList<>()));
		AccountDTO inputAccountDTO =  new AccountDTO.Builder().setAccountName("Anamika")
				.setAccountNumber("1")
				.setBalance(BigDecimal.valueOf(1022.1))
				.setId(1L)
				.build();
		AccountDTO savedAccountDTO = new  AccountDTO.Builder().setAccountName("Anamika")
				.setAccountNumber("1")
				.setBalance(BigDecimal.valueOf(1022.1))
				.setId(1L)
				.build();
		ObjectMapper objectMapper = new ObjectMapper();
		when(accountService.saveAccount(any(AccountDTO.class))).thenReturn(savedAccountDTO);

		mockMvc.perform(MockMvcRequestBuilders.post("/api/accounts")
						.header("Authorization", "Bearer "+bearerToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(inputAccountDTO)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value(savedAccountDTO.getAccountNumber()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedAccountDTO.getId()));
	}

	@Test
	public void testUpdateAccount() throws Exception {
		Long accountId = 1L;
		AccountDTO inputAccountDTO =  new AccountDTO.Builder().setAccountName("Anamika")
				.setAccountNumber("1")
				.setBalance(BigDecimal.valueOf(1022.1))
				.setId(1L)
				.build();
		AccountDTO updatedAccountDTO = new  AccountDTO.Builder().setAccountName("Anamika Gupta")
				.setAccountNumber("1")
				.setBalance(BigDecimal.valueOf(1022.1))
				.setId(1L)
				.build();
		ObjectMapper objectMapper = new ObjectMapper();
		String bearerToken = authTokenUtil.generateToken(new User("user", "password", new ArrayList<>()));

		when(accountService.updateAccount(any(AccountDTO.class), any(Long.class))).thenReturn(updatedAccountDTO);

		mockMvc.perform(MockMvcRequestBuilders.put("/api/accounts/{id}", inputAccountDTO.getId())
						.header("Authorization", "Bearer "+bearerToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(inputAccountDTO)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value(updatedAccountDTO.getAccountNumber()))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(updatedAccountDTO.getId()));
	}

	@Test
	public void testGetAccounts() throws Exception {
		int page = 0;
		int size = 2;

		List<AccountDTO> li = Collections.emptyList();
		when(accountService.getAllAccounts(PageRequest.of(page, size)))
				.thenReturn(li);
		String bearerToken = authTokenUtil.generateToken(new User("user", "password", new ArrayList<>()));
		mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts")
						.header("Authorization", "Bearer "+bearerToken)
						.param("pageSize", String.valueOf(size))
						.param("page", String.valueOf(page)))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void testGetAccountsByAccountNumber() throws Exception {
		String accountNumber = "1";

		Account account =new Account.Builder().setAccountName("Anamika")
				.setAccountNumber("1")
				.setBalance(BigDecimal.valueOf(1022.1))
				.setId(1L)
				.build();
		List<Account> accounts = new ArrayList<>();
		accounts.add(account);

		AccountDTO inputAccountDTO =  new AccountDTO.Builder().setAccountName("Anamika")
				.setAccountNumber("1")
				.setBalance(BigDecimal.valueOf(1022.1))
				.setId(1L)
				.build();
		String bearerToken = authTokenUtil.generateToken(new User("user", "password", new ArrayList<>()));


		when(accountService.getAccountByAccountNumber(accountNumber))
				.thenReturn(accounts);
		mockMvc.perform(MockMvcRequestBuilders.get("/api/accounts/account")
						.header("Authorization", "Bearer "+bearerToken)
						.param("accountNumber", accountNumber))

				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].accountNumber").value(inputAccountDTO.getAccountNumber()))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].accountName").value(inputAccountDTO.getAccountName()));
	}
}
