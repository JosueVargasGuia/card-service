package com.nttdata.card.service.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
 

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AccountPayable { 
	private TypeAccount typeAccount;//BankAccounts o CreditAccount
	private Long idAccount;// Identificador de cuenta que toma los id de BankAccounts.idBankAccount o CreditAccount.idCreditAccount	
}
