package com.nttdata.card.service.FeignClient.FallBackImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nttdata.card.service.FeignClient.BankAccountFeignClient;
import com.nttdata.card.service.model.BankAccounts;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class BankAccountFeignClientFallBack implements BankAccountFeignClient{

	@Value("${api.account-service.uri}")
	private String accountService;
	

	public BankAccounts findById(Long idBankAccount) {
		log.info("BankAccountFeignClientFallBack -> " + accountService+"/"+idBankAccount);
		return null;
	}


 
	public Map<String, Object> balanceInquiry(BankAccounts bankAccounts) {
		log.info("BankAccountFeignClientFallBack -> " + accountService+"/balanceInquiry ["+bankAccounts+"]");
		Map<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("status", "error");
		return hashMap;
		 
	}



	@Override
	public List<BankAccounts> findAllByAccount(Long idAccount) {
		log.info("BankAccountFeignClientFallBack -> "+"/findAllByAccount ["+idAccount+"]");
		return null;
	}



	@Override
	public BankAccounts findByIdForExample(BankAccounts find) {
		log.info("BankAccountFeignClientFallBack -> "+"/findByIdForExample ["+find.toString()+"]");
		return null;
	}
}
