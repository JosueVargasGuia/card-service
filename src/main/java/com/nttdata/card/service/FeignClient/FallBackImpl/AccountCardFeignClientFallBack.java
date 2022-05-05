package com.nttdata.card.service.FeignClient.FallBackImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nttdata.card.service.FeignClient.AccountCardFeignClient;
import com.nttdata.card.service.model.AccountCard;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class AccountCardFeignClientFallBack implements AccountCardFeignClient {
	@Value("${api.account-card-service.uri}")
	String serviceUri;

	 
	public AccountCard save(AccountCard accountCard) {
		log.info("AccountCardFeignClientFallBack -> " + serviceUri );
		return null;
	}
	
	public List<AccountCard> findByIdCredit(Long idCard) {
		// TODO Auto-generated method stub
		log.info("AccountCardFeignClientFallBack -> " + serviceUri+"/findByidCard/"+idCard );
		return null;
	}

	@Override
	public AccountCard findByIdForExample(AccountCard accountCard) {
		log.info("AccountCardFeignClientFallBack -> " + serviceUri+"/findByIdForExample/");
		return null;
	}

}
