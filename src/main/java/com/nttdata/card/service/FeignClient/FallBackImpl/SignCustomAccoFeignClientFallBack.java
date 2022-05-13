package com.nttdata.card.service.FeignClient.FallBackImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nttdata.card.service.FeignClient.SignCustomAccoFeignClient;
import com.nttdata.card.service.model.SignatoriesCustomerAccounts;

import lombok.extern.log4j.Log4j2;
 
@Log4j2
@Component
public class SignCustomAccoFeignClientFallBack implements SignCustomAccoFeignClient{
	@Value("${api.SignCustAccount-service.uri}")
	private String serviceUrl;

	
	public SignatoriesCustomerAccounts findById(Long idHolderAccount) {
		log.info("SignCustomAccoFeignClientFallBack -> " + serviceUrl);
		return null;
	}
}
