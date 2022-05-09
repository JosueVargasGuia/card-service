package com.nttdata.card.service.model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
 public final class BankAccounts extends Account{

	private Long idBankAccount;
	private Long idProduct;
}
