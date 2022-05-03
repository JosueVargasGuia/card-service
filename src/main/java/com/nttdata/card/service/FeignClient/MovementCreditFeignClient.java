package com.nttdata.card.service.FeignClient; 
 
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nttdata.card.service.FeignClient.FallBackImpl.MovementCreditFeignClientFallBack;

import com.nttdata.card.service.model.CreditAccount;

@FeignClient(name = "${api.movementCredit-service.uri}", fallback = MovementCreditFeignClientFallBack.class)
public interface MovementCreditFeignClient {

	@PostMapping(value = "/balanceInquiry")
	Map<String, Object> balanceInquiry(@RequestBody CreditAccount creditAccount); 
}
