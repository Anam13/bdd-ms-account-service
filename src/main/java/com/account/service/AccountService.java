package com.account.service;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.account.constant.ErrorConstants;
import com.account.entity.Account;
import com.account.exception.ApiErrorException;
import com.account.model.AccountDTO;
import com.account.repo.AccountRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class AccountService {

	@Autowired
	private AccountRepository accountRepository;

	public AccountDTO saveAccount(AccountDTO accountDTO) {
		Account account = new Account.Builder().setAccountName(accountDTO.getAccountName())
				.setAccountNumber(accountDTO.getAccountNumber())
				.setCreatedDate(LocalDateTime.now())
				.setUpdatedDate(LocalDateTime.now())
				.setBalance(accountDTO.getBalance()).build();
		return mapToAccountDTO(accountRepository.save(account));
	}

	public AccountDTO updateAccount(AccountDTO accountDTO, Long id) {
		if (id != null && accountRepository.findById(id).isPresent()) {
			Account account = accountRepository.findById(id).get();			
			account.setAccountName(accountDTO.getAccountName());
			account.setAccountNumber(accountDTO.getAccountNumber());
			account.setBalance(accountDTO.getBalance());
			account.setUpdatedDate(LocalDateTime.now());
			return mapToAccountDTO(accountRepository.save(account));
		} else
			throw new ApiErrorException(HttpStatus.BAD_REQUEST.value(), ErrorConstants.ACCOUNT_NOT_UPDATE_CODE,
					ErrorConstants.ACCOUNT_CANT_UPDATE + accountDTO.getAccountNumber());
	}

	public List<AccountDTO> getAllAccounts(Pageable paging) {
		Iterable<Account> accounts = accountRepository.findAll(paging);
		List<AccountDTO> accountsDTO = new ArrayList<>();

		accounts.forEach(account -> {
			accountsDTO.add(new AccountDTO.Builder().setId(account.getId()).setAccountName(account.getAccountName())
					.setAccountNumber(account.getAccountNumber()).setCreatedDate(account.getCreatedDate())
					.setUpdatedDate(account.getUpdatedDate()).setBalance(account.getBalance()).build());
		});

		return accountsDTO;
	}

	public List<Account> getAccountByAccountNumber(String accountNumber) {
		Optional<List<Account>> accountList = accountRepository.findAllByAccountNumber(accountNumber);
		if (accountList.isPresent() && !accountList.get().isEmpty())
			return accountList.get();
		else
			throw new ApiErrorException(HttpStatus.BAD_REQUEST.value(),
					ErrorConstants.ACCOUNT_NOT_FOUND_ACC_NUMBER_CODE,
					ErrorConstants.ACCOUNT_NOT_FOUND_ACC_NUMBER + accountNumber);
	}

	public List<Account> getAccountByAccountName(String accountName) {
		Optional<List<Account>> accountList = accountRepository.findAllByAccountName(accountName);
		if (accountList.isPresent() && !accountList.get().isEmpty())
			return accountList.get();
		else
			throw new ApiErrorException(HttpStatus.BAD_REQUEST.value(),
					ErrorConstants.ACCOUNT_NAME_NOT_FOUND_ACC_NAME_CODE,
					ErrorConstants.ACCOUNT_NAME_NOT_FOUND_ACC_NAME + accountName);
	}

	AccountDTO mapToAccountDTO(Account account) {
		return new AccountDTO.Builder().setAccountName(account.getAccountName())
				.setAccountNumber(account.getAccountNumber()).setCreatedDate(account.getCreatedDate())
				.setUpdatedDate(account.getUpdatedDate()).setBalance(account.getBalance()).setId(account.getId())
				.build();
	}

	Account mapAccountDTOToAccount(AccountDTO accountDTO) {
		return new Account.Builder().setAccountName(accountDTO.getAccountName())
				.setAccountNumber(accountDTO.getAccountNumber()).setCreatedDate(accountDTO.getCreatedDate())
				.setUpdatedDate(accountDTO.getUpdatedDate()).setBalance(accountDTO.getBalance()).build();

	}

}
