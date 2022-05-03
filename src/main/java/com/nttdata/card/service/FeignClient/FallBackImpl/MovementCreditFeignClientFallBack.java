package com.nttdata.card.service.FeignClient.FallBackImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.nttdata.card.service.FeignClient.MovementCreditFeignClient;
import com.nttdata.card.service.model.CreditAccount;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class MovementCreditFeignClientFallBack implements MovementCreditFeignClient {
	@Value("${api.movementCredit-service.uri}")
	String service;

	public Map<String, Object> balanceInquiry(CreditAccount creditAccount) {
		log.info("MovementCreditFeignClientFallBack -> " + service + "/balanceInquiry [" + creditAccount + "]");
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("status", "error");
		return hashMap;
	}
}
