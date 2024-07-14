package com.account.repo;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.account.entity.Account;

public interface AccountRepository extends JpaRepository <Account, Long> {

	Optional<List<Account>> findAllByAccountNumber(String accountNumber);
	
	Optional<List<Account>> findAllByAccountName(String accountNumber);

}