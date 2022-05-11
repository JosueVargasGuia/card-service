package com.nttdata.card.service.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.nttdata.card.service.entity.Card;
import com.nttdata.card.service.entity.CardType;
import com.nttdata.card.service.model.AccountCard;
import com.nttdata.card.service.sevice.CardService;
import com.nttdata.wallet.model.AssociatedWallet;
import com.nttdata.wallet.model.CardWallet;
import com.nttdata.wallet.model.Wallet;
import com.nttdata.card.service.model.TypeAccount;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class WalletConsumer {
	@Value("${api.kafka-uri.card-topic-respose}")
	String carTopicRespose;
	@Autowired
	CardService cardService;
	@Autowired
	KafkaTemplate<String, CardWallet> kafkaTemplate;

	/** Methodo que realiza la operacion y validacion de asignacion de la tarjeta */
	@KafkaListener(topics = "${api.kafka-uri.card-topic}", groupId = "group_id")
	public void walletConsumer(CardWallet cardWallet) {
		log.info("walletConsumer:" + cardWallet.toString());
		CardWallet cardWalletResponse = new CardWallet();
		Wallet wallet = new Wallet();
		Card cardRes = new Card();
		wallet.setIdWallet(cardWallet.getWallet().getIdWallet());
		cardRes.setCardNumber(cardWallet.getCard().getCardNumber());

		Card cardFind = new Card();
		cardFind.setCardNumber(cardWallet.getCard().getCardNumber());
		cardFind.setCardType(CardType.debitCard);
		Card card = null;
		card = this.cardService.findByCardForExample(cardFind).blockOptional().orElse(null);
		wallet.setAssociatedWallet(AssociatedWallet.CardNotAssociated);
		if (card != null) {
			log.info("card:" + card.toString());
			AccountCard accountCard = new AccountCard();
			accountCard.setIdCard(card.getIdCard());
			accountCard.setIsMainAccount(true);
			accountCard.setTypeAccount(TypeAccount.BankAccounts);
			accountCard = this.cardService.findByAccountCardForExample(accountCard);
			if (accountCard != null) {
				log.info("accountCard:" + accountCard);
				wallet.setIdCard(card.getIdCard());
				wallet.setIdBankAccount(accountCard.getIdAccount());
				wallet.setCardType(card.getCardType());
				wallet.setAssociatedWallet(AssociatedWallet.AssociatedCard);
				wallet.setAssociatedWalletMessage("Cartera asociada a la cuenta Nro:"+cardFind.getCardNumber());
			} else {
				wallet.setAssociatedWalletMessage(
						"Cuenta principal de tipo " + TypeAccount.BankAccounts + " no encontrada");
			}
		} else {
			wallet.setAssociatedWalletMessage("tarjeta Nro:"
					+ cardWallet.getCard().getCardNumber() + "de tipo " + CardType.debitCard + " no encontrada");
		}
		cardWalletResponse.setWallet(wallet);
		cardWalletResponse.setCard(cardRes);
		log.info("Send kafka " + carTopicRespose);
		log.info("Send kafka[CardWallet] " + cardWalletResponse.toString());
		kafkaTemplate.send(carTopicRespose, cardWalletResponse);
	}
}
