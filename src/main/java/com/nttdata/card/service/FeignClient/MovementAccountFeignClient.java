package com.nttdata.card.service.FeignClient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nttdata.card.service.FeignClient.FallBackImpl.MovementAccountFeignClientFallBack;
import com.nttdata.card.service.model.BankAccounts;
import com.nttdata.card.service.model.MovementAccount;

@FeignClient(name = "${api.movement-account-service.uri}"
 , fallback = MovementAccountFeignClientFallBack.class
)
public interface MovementAccountFeignClient {
	@PostMapping(value = "/balanceInquiry")
	Map<String, Object> balanceInquiry(@RequestBody BankAccounts bankAccounts);

	@PostMapping(value = "/recordAccount")
	Map<String, Object> recordAccount(@RequestBody MovementAccount movementAccount);

	@DeleteMapping("/{idBankAccount}")
	void delete(@PathVariable("idBankAccount") Long idBankAccount);
}
