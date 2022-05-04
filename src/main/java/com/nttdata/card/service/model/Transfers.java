package com.nttdata.card.service.model;

 
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Transfers {
	TypeTransfer typeTransfer;
	AccountTransfers originAccount;
	AccountTransfers targetAccount;
	Double amount;
	String interbankAccountCode;
}
