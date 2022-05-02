package com.nttdata.card.service.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nttdata.card.service.FeignClient.FallBackImpl.SignCustomAccoFeignClientFallBack;
import com.nttdata.card.service.model.SignatoriesCustomerAccounts;

@FeignClient(name = "${api.SignCustAccount-service.uri}", fallback = SignCustomAccoFeignClientFallBack.class)
public interface SignCustomAccoFeignClient {

	@GetMapping("/{idSignCustAccount}")
	SignatoriesCustomerAccounts findById(@PathVariable(name = "idSignCustAccount") Long idSignCustAccount); 
	
}
