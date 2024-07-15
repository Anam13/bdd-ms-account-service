package com.account.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "account")
@Data
@Builder
@AllArgsConstructor
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String accountNumber;

	@Column(nullable = false)
	private String accountName;	

	@Column(nullable = false)
	private BigDecimal balance;
	
	
	private LocalDateTime createdDate;

	private LocalDateTime updatedDate;
    @PrePersist
    protected void onCreate() {
    	createdDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
    	updatedDate = LocalDateTime.now();
    }

	private Account() {
	}

	public Long getId() {
		return id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public String getAccountName() {
		return accountName;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public LocalDateTime getUpdatedDate() {
		return updatedDate;
	}
	

	public void setId(Long id) {
		this.id = id;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public void setUpdatedDate(LocalDateTime updatedDate) {
		this.updatedDate = updatedDate;
	}


	public static class Builder {

		private Long id;

		private String accountNumber;

		private String accountname;

		private LocalDateTime createdDate;

		private BigDecimal balance;

		private LocalDateTime updatedDate;

		public Builder() {
		}

		public Builder setId(Long id) {
			this.id = id;
			return this;
		}

		public Builder setAccountNumber(String accountNumber) {
			this.accountNumber = accountNumber;
			return this;
		}

		public Builder setAccountName(String accountname) {
			this.accountname = accountname;
			return this;
		}

		public Builder setCreatedDate(LocalDateTime createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public Builder setBalance(BigDecimal balance) {
			this.balance = balance;
			return this;
		}

		public Builder setUpdatedDate(LocalDateTime updatedDate) {
			this.updatedDate = updatedDate;
			return this;
		}

		public Account build() {
			Account account = new Account();
			account.id = this.id;
			account.accountNumber = this.accountNumber;
			account.accountName = this.accountname;
			account.balance = this.balance;
			account.createdDate = this.createdDate;
			account.updatedDate = this.updatedDate;
			return account;
		}
	}

}