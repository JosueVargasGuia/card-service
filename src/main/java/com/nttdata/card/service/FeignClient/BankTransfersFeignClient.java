package com.nttdata.card.service.FeignClient;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nttdata.card.service.FeignClient.FallBackImpl.BankTransfersFeignClientFallback;
import com.nttdata.card.service.model.Transfers;

 

@FeignClient(name = "${api.bank-transfers-service.uri}",fallback = BankTransfersFeignClientFallback.class)
public interface BankTransfersFeignClient {
	@PostMapping("/wireTransfer")
	Map<String, Object> wireTransfer(@RequestBody Transfers transfers);
}
