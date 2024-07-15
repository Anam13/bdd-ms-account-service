package com.account.controller;

import com.account.entity.*;
import com.account.model.*;
import com.account.service.AccountService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@SecurityRequirement(name = "Authorization")
@RequestMapping("/api/accounts")

public class AccountController {
	/**
	 * 
	 */

	@Autowired
	private AccountService accountService;

	/**
	 * @param accountDTO
	 * @return
	 * @throws Exception
	 */

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody AccountDTO accountDTO) throws Exception {		
		return new ResponseEntity<>(accountService.saveAccount(accountDTO), HttpStatus.CREATED);
	}

	/**
	 * @param id
	 * @param accountDTO
	 * @return
	 * @throws Exception
	 */

	@PutMapping(path = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
			MediaType.APPLICATION_JSON_VALUE })

	public ResponseEntity<AccountDTO> updateAccount(@PathVariable(required = true)Long id, @Valid @RequestBody AccountDTO accountDTO)
			throws Exception {		
		return new ResponseEntity<>(accountService.updateAccount(accountDTO,id), HttpStatus.OK);
	}

	/**
	 * 
	 * @param pageSize
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@GetMapping()
	public ResponseEntity<List<AccountDTO>> getAccounts(
			@RequestParam(defaultValue = "2", required = false) Integer pageSize,
			@RequestParam(defaultValue = "0", required = false) Integer page) throws Exception {

		Pageable paging = PageRequest.of(page, pageSize);
		List<AccountDTO> accountsDTO = accountService.getAllAccounts(paging);
		return new ResponseEntity<>(accountsDTO, HttpStatus.OK);
	}

	/**
	 * 
	 * @param accountNumber
	 * @return
	 * @throws Exception
	 */

	@GetMapping("/account")
	public ResponseEntity<List<Account>> getAccountsByAccountNumber(
			@RequestParam(required = false) String accountNumber, @RequestParam(required = false) String accountName)
			throws Exception {
		if (accountNumber != null) {
			return new ResponseEntity<List<Account>>(accountService.getAccountByAccountNumber(accountNumber),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<List<Account>>(accountService.getAccountByAccountName(accountName),
					HttpStatus.OK);
		}

	}

}
