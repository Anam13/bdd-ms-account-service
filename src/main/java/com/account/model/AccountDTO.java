package com.account.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Data
@Builder
@AllArgsConstructor
public class AccountDTO {

	private Long id;

	@NotBlank(message ="must not be blank" )
	private String accountNumber;
	@NotBlank(message ="must not be blank" )
	private String accountName;	
	@NotNull(message ="must not be null" )
	private BigDecimal balance;
	
	private LocalDateTime createdDate;

	private LocalDateTime updatedDate;

	private AccountDTO() {
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

		private String accountName;

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


		public Builder setAccountName(String accountName) {
			this.accountName = accountName;
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

		public AccountDTO build() {
			AccountDTO accountDTO = new AccountDTO();
			accountDTO.id = this.id;
			accountDTO.accountNumber = this.accountNumber;
			accountDTO.accountName = this.accountName;
			accountDTO.balance = this.balance;
			accountDTO.createdDate = this.createdDate;
			accountDTO.updatedDate = this.updatedDate;
			return accountDTO;
		}
	}
}