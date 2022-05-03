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
public class FinancialOperation {
	private String cardNumber;
	private TypeOperation typeOperation;
	private Double amount;
	private AccountPayable accountPayable;
}
