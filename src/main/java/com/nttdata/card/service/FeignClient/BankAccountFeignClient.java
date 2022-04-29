package com.nttdata.card.service.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.nttdata.card.service.FeignClient.FallBackImpl.BankAccountFeignClientFallBack;
import com.nttdata.card.service.model.Account;
import com.nttdata.card.service.model.BankAccounts;

@FeignClient(name = "${api.account-service.uri}", fallback = BankAccountFeignClientFallBack.class)
public interface BankAccountFeignClient {

	@GetMapping("/{idBankAccount}")
	BankAccounts findById(@PathVariable("idBankAccount") Long idBankAccount);

}
