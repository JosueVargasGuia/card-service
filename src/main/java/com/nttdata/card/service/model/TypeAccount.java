package com.nttdata.card.service.model;

/** Indica que tipo de objeto estamos guardando */
public enum TypeAccount {
	CreditAccount, BankAccounts,BankTransfers, /** Indica que es una tranferencia interbancaria */externalAccount
	;
}
