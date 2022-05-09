package com.nttdata.card.service.consumer;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.nttdata.card.service.entity.Card;
import com.nttdata.card.service.model.AccountCard;
import com.nttdata.card.service.sevice.CardService;
import com.nttdata.wallet.model.CardWallet;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class WalletConsumer {
	@Value("${api.kafka-uri.card-topic-respose}")
	String carTopicRespose;
	@Autowired
	CardService cardService;

	/** Methodo que realiza la operacion y validacion de asignacion de la tarjeta */
	@KafkaListener(topics = "${api.kafka-uri.card-topic}", groupId = "group_id")
	public void walletConsumer(CardWallet cardWallet) {
		log.info("walletConsumer:" + cardWallet.toString());
		Card card = new Card();
		card.setCardNumber(cardWallet.getCard().getCardNumber());
		card = this.cardService.findByCardForExample(card).blockOptional().get();
		AccountCard accountCard = new AccountCard();
		accountCard.setIdCard(cardWallet.getCard().getIdCard());
		accountCard.setIsMainAccount(true);
		accountCard = this.cardService.findByAccoundCardForExample(accountCard);
		log.info("accountCard:"+accountCard);
	}
}
