package com.nttdata.card.service.FeignClient.FallBackImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nttdata.card.service.FeignClient.HolderAccountFeignClient;
import com.nttdata.card.service.model.HolderAccount;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class HolderAccountFeignClientFallBack implements HolderAccountFeignClient {
	@Value("${api.holder-account-service.uri}")
	private String serviceUrl;


	public HolderAccount findById(Long idHolderAccount) {
		log.info("HolderAccountFeignClientFallBack -> " + serviceUrl);
		return null;
	}
}
