package com.nttdata.wallet.model;

 

import com.nttdata.card.service.entity.Card;

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
public class CardWallet {
	Wallet wallet;
	Card card;
}
