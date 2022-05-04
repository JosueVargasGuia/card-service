package com.nttdata.card.service.FeignClient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nttdata.card.service.FeignClient.FallBackImpl.MovementCreditFeignClientFallBack;

import com.nttdata.card.service.model.CreditAccount;
import com.nttdata.card.service.model.MovementCredit;

@FeignClient(name = "${api.movementCredit-service.uri}"
 , fallback = MovementCreditFeignClientFallBack.class
)
public interface MovementCreditFeignClient {

	@PostMapping(value = "/balanceInquiry")
	Map<String, Object> balanceInquiry(@RequestBody CreditAccount creditAccount);

	@PostMapping(value = "/recordsMovement")
	Map<String, Object> recordsMovement(@RequestBody MovementCredit movementCredit);

	@DeleteMapping("/{idMovementCredit}")
	void delete(@PathVariable("idMovementCredit") Long idMovementCredit);
}
