package com.nttdata.card.service.FeignClient.FallBackImpl;

 

import org.springframework.beans.factory.annotation.Value;
 
import org.springframework.stereotype.Component;

import com.nttdata.card.service.FeignClient.CreditAccountFeignClient;
 
import com.nttdata.card.service.model.CreditAccount;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
@Component
public class CreditAccountFeignClientFallBack implements CreditAccountFeignClient {
	
	@Value("${api.credit-service.uri}")
	private String accountService;

	public CreditAccount findById(Long idCreditAccount) {
		log.info("CreditAccountFeignClientFallBack -> " + accountService + "/" + idCreditAccount);
		return null;
	}

	@Override
	public CreditAccount findByIdForExample(CreditAccount find) {
		log.info("CreditAccountFeignClientFallBack -> "+"/findByIdForExample ["+find.toString()+"]");
		return null;
	}

 

}
