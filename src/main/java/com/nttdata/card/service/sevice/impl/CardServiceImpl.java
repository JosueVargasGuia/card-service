package com.nttdata.card.service.sevice.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nttdata.card.service.FeignClient.AccountCardFeignClient;
import com.nttdata.card.service.FeignClient.BankAccountFeignClient;
import com.nttdata.card.service.FeignClient.CreditAccountFeignClient;
import com.nttdata.card.service.FeignClient.HolderAccountFeignClient;
import com.nttdata.card.service.FeignClient.SignCustomAccoFeignClient;
import com.nttdata.card.service.FeignClient.TableIdFeignClient;
import com.nttdata.card.service.entity.Card;
import com.nttdata.card.service.model.Account;
import com.nttdata.card.service.model.AccountCard;
import com.nttdata.card.service.model.BankAccounts;
import com.nttdata.card.service.model.CreditAccount;
import com.nttdata.card.service.model.HolderAccount;
import com.nttdata.card.service.model.SignatoriesCustomerAccounts;
import com.nttdata.card.service.model.TypeAccount;
import com.nttdata.card.service.repository.CardRepository;
import com.nttdata.card.service.sevice.CardService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class CardServiceImpl implements CardService {
	@Autowired
	CardRepository cardRepository;
	@Autowired
	TableIdFeignClient tableIdFeignClient;
	@Autowired
	BankAccountFeignClient bankAccountFeignClient;
	@Autowired
	CreditAccountFeignClient creditAccountFeignClient;
	@Autowired
	HolderAccountFeignClient holderAccountFeignClient;
	@Autowired
	SignCustomAccoFeignClient signCustomAccoFeignClient;
	@Autowired
	AccountCardFeignClient cardFeignClient;

	@Override
	public Flux<Card> findAll() {
		return cardRepository.findAll();
	}

	@Override
	public Mono<Card> findById(Long idCard) {
		return cardRepository.findById(idCard);
	}

	@Override
	public Mono<Card> save(Card card) {
		Long idCard = tableIdFeignClient.generateKey(Card.class.getSimpleName());
		log.info("Key:" + idCard);
		if (idCard >= 1) {
			card.setCreationDate(Calendar.getInstance().getTime());
			card.setIdCard(idCard);
		} else {
			return Mono.error(new InterruptedException("Servicio no disponible:" + Card.class.getSimpleName()));
		}
		return cardRepository.insert(card);
	}

	@Override
	public Mono<Card> update(Card card) {
		card.setDateModified(Calendar.getInstance().getTime());
		return cardRepository.save(card);
	}

	@Override
	public Mono<Void> delete(Long idCard) {
		return cardRepository.deleteById(idCard);
	}

	/** Registra una tarjeta */
	@Override
	public Mono<Map<String, Object>> registerCard(Card card) {
		// Cuenta de bancarias o productos pasivos
		BankAccounts bankAccount = bankAccountFeignClient.findById(card.getIdAccount());
		// Cuentas de credito o product activos
		CreditAccount creditAccount = creditAccountFeignClient.findById(card.getIdAccount());
		// Buscando titular de la cuenta
		HolderAccount holderAccount = holderAccountFeignClient.findById(card.getIdHolderAccount());
		// Buscando firma autorisada
		SignatoriesCustomerAccounts signatoriesCustomerAccounts = signCustomAccoFeignClient
				.findById(card.getIdSignCustAccount());
		Map<String, Object> map = new HashMap<>();
		boolean valid = true;
		if (bankAccount == null && creditAccount == null) {
			valid = false;
			map.put("Account", "La cuenta no existe.");
		}
		if (holderAccount == null && signatoriesCustomerAccounts == null) {
			valid = false;
			map.put("HolderSignature", "Se requiere una firma autorisada o titular de la cuenta.");
		}
		if (valid) {
			if (card.getPassword() == null) {
				card.setPassword(numberRandoms(4, false));
			}
			if (card.getCardNumber() == null) {
				card.setCardNumber(numberRandoms(16, true));
			}
			if (card.getCvv() == null) {
				card.setCvv(numberRandoms(3, false));
			}
			return this.save(card).map(objCard -> {
				map.put("Card", objCard);
				return map;
			});
		} else {
			return Mono.empty();
		}

	}

	String numberRandoms(int cant, boolean isCard) {
		StringBuilder builder = new StringBuilder();
		for (int i = 1; i < cant + (isCard ? 4 : 1); i++) {
			if (i % 5 == 0 && isCard) {
				builder.append("-");
			} else {
				builder.append((int) (Math.random() * 9));
			}

		}
		return builder.toString();
	}

	@Override
	public Mono<Map<String, Object>> associateAccountCard(AccountCard accountCard) {
		Map<String, Object> map = new HashMap<>();
		BankAccounts bankAccount = null;
		if (accountCard.getTypeAccount() == TypeAccount.BankAccounts) {
			// Cuenta de bancarias o productos pasivos
			bankAccount = bankAccountFeignClient.findById(accountCard.getIdAccount());
		}
		CreditAccount creditAccount = null;
		if (accountCard.getTypeAccount() == TypeAccount.CreditAccount) {
			// Cuentas de credito o product activos
			creditAccount = creditAccountFeignClient.findById(accountCard.getIdAccount());
		}
		if (bankAccount == null && creditAccount == null) {
			map.put("Account", "La cuenta no existe.");
			map.put("status", "error");
		} else {
			Long count = Flux.fromIterable(cardFeignClient.findByIdCredit(accountCard.getIdCard()))
					.filter(e->e.getIdAccount()==accountCard.getIdAccount())
					.collect(Collectors.counting()).blockOptional().orElse(Long.valueOf(0));
			//log.info("count:"+count);
			if (count <= 0) {
				AccountCard accountCardRespose = cardFeignClient.save(accountCard);
				if (accountCardRespose == null) {
					map.put("AccountCard", "El servicio de Account-Card no esta disponible.");
					map.put("status", "error");
				} else {
					map.put("status", "success");
				}
			} else {
				map.put("AccountCard", "La tarjeta ya tiene asignada la cuenta ingresada.");
				map.put("status", "error");
			}
		}

		return Mono.just(map);
	}
}
