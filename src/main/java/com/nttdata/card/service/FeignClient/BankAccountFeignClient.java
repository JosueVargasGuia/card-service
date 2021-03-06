package com.nttdata.card.service.FeignClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.nttdata.card.service.FeignClient.FallBackImpl.BankAccountFeignClientFallBack;
import com.nttdata.card.service.model.BankAccounts;

@FeignClient(name = "${api.account-service.uri}", fallback = BankAccountFeignClientFallBack.class)
public interface BankAccountFeignClient {

	@GetMapping("/{idBankAccount}")
	BankAccounts findById(@PathVariable("idBankAccount") Long idBankAccount);
	
	@GetMapping(value="/findAllByAccount/{idAccount}")
	List<BankAccounts> findAllByAccount(@PathVariable("idAccount") Long idAccount);

	@PostMapping("/findByIdForExample")
	BankAccounts findByIdForExample(BankAccounts find);
 
}
