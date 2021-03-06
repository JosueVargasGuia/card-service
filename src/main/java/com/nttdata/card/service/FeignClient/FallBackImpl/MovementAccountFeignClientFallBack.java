package com.nttdata.card.service.FeignClient.FallBackImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nttdata.card.service.FeignClient.MovementAccountFeignClient;
import com.nttdata.card.service.model.BankAccounts;
import com.nttdata.card.service.model.MovementAccount;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class MovementAccountFeignClientFallBack implements MovementAccountFeignClient {
	@Value("${api.movement-account-service.uri}")
	String service;

	public Map<String, Object> balanceInquiry(BankAccounts bankAccounts) {
		log.info("MovementAccountFeignClientFallBack -> " + service + "/balanceInquiry [" + bankAccounts + "]");
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("status", "error");
		return hashMap;
	}

	 
	public Map<String, Object> recordAccount(MovementAccount movementAccount) {
		log.info("MovementAccountFeignClientFallBack -> " + service + "/recordAccount [" + movementAccount + "]");
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("status", "error");
		return hashMap;
	}


	@Override
	public void delete(Long idBankAccount) {
		log.info("MovementAccountFeignClientFallBack -> " + service + "/" + idBankAccount );	
		
	}
}
