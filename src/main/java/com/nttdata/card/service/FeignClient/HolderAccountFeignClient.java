package com.nttdata.card.service.FeignClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.nttdata.card.service.FeignClient.FallBackImpl.HolderAccountFeignClientFallBack;
import com.nttdata.card.service.model.HolderAccount;
 

@FeignClient(name = "${api.holder-account-service.uri}", fallback = HolderAccountFeignClientFallBack.class)
public interface HolderAccountFeignClient {
 
	@GetMapping("/{idHolderAccount}")
	HolderAccount findById(@PathVariable(name = "idHolderAccount") Long idHolderAccount);
}
