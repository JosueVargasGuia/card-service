package com.nttdata.card.service.FeignClient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nttdata.card.service.FeignClient.FallBackImpl.MovementAccountFeignClientFallBack;
import com.nttdata.card.service.model.BankAccounts;

@FeignClient(name = "${api.movement-account-service.uri}", fallback = MovementAccountFeignClientFallBack.class)
public interface MovementAccountFeignClient {
	@PostMapping(value = "/balanceInquiry")
	Map<String, Object> balanceInquiry(@RequestBody BankAccounts bankAccounts);
}
