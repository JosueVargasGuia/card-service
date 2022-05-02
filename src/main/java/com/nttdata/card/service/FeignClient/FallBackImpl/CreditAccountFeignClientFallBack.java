package com.nttdata.card.service.FeignClient.FallBackImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nttdata.card.service.FeignClient.CreditAccountFeignClient;
import com.nttdata.card.service.model.Account;
import com.nttdata.card.service.model.BankAccounts;
import com.nttdata.card.service.model.CreditAccount;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class CreditAccountFeignClientFallBack implements CreditAccountFeignClient {
	@Value("${api.credit-service.uri}")
	private String accountService;

	public CreditAccount findById(Long idCreditAccount) {
		log.info("CreditAccountFeignClientFallBack -> " + accountService + "/" + idCreditAccount);
		return null;
	}

}
