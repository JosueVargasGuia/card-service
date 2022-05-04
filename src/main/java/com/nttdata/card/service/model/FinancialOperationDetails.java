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
public class FinancialOperationDetails {
	private TypeAccount typeAccount;
	private Long idAccount;
	private Double balanceTake;
	private Long idMovement;
	private String status;
	/*Tranferencias*/
	private StatusTransfer isTransfer;
	private TypeAccount typeOperacion;
	private String interbankAccountCode;
}
