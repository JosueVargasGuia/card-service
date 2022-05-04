package com.nttdata.card.service.model;

import java.util.ArrayList;
import java.util.List;

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
public class FinancialOperation {
	private String cardNumber;
	private TypeOperation typeOperation;
	private Double amount;
	private Double amountOperation;
	private AccountPayable accountPayable;
	private Double balanceTake;
	List<FinancialOperationDetails>operationDetails=new ArrayList<FinancialOperationDetails>();
}
