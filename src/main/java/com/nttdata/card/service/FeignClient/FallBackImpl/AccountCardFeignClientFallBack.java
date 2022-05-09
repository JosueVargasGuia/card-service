package com.nttdata.card.service.FeignClient.FallBackImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nttdata.card.service.FeignClient.AccountCardFeignClient;
import com.nttdata.card.service.entity.Card;
import com.nttdata.card.service.model.AccountCard;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class AccountCardFeignClientFallBack implements AccountCardFeignClient {
	@Value("${api.account-card-service.uri}")
	String serviceUri;

	 
	public AccountCard save(AccountCard accountCard) {
		log.info("CreditAccountFeignClientFallBack -> " + serviceUri );
		return null;
	}


	 
	public List<AccountCard> findByIdCredit(Long idCard) {
		// TODO Auto-generated method stub
		log.info("CreditAccountFeignClientFallBack -> " + serviceUri+"/findByidCard/"+idCard );
		return null;
	}



	@Override
	public AccountCard  findByAccoundCardForExample(AccountCard accountCard) {
		// TODO Auto-generated method stub
		log.info("CreditAccountFeignClientFallBack -> " + serviceUri+"/findByExample :"+accountCard.toString()  );
		return null;
	}

}
