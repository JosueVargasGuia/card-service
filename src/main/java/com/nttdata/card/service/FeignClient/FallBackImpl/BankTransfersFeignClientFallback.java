package com.nttdata.card.service.FeignClient.FallBackImpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nttdata.card.service.FeignClient.BankTransfersFeignClient;
import com.nttdata.card.service.model.Transfers;

import lombok.extern.log4j.Log4j2;
@Log4j2
@Component
public class BankTransfersFeignClientFallback implements BankTransfersFeignClient {
	@Value("${api.bank-transfers-service.uri}")
	String service;
	public Map<String, Object> wireTransfer(Transfers transfers) {
		// TODO Auto-generated method stub
		log.info("BankTransfersFeignClientFallback -> " + service+"/wireTransfer ["+transfers+"]");
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("status", "error");
		return hashMap;
	}

}
