package com.nttdata.card.service.FeignClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.nttdata.card.service.FeignClient.FallBackImpl.AccountCardFeignClientFallBack;
import com.nttdata.card.service.entity.Card;
import com.nttdata.card.service.model.AccountCard;

import reactor.core.publisher.Mono;

@FeignClient(name = "${api.account-card-service.uri}", fallback = AccountCardFeignClientFallBack.class)
public interface AccountCardFeignClient {

	@PostMapping
	AccountCard save(AccountCard accountCard);

	@GetMapping("/findByidCard/{idCard}")
	List<AccountCard> findByIdCredit(@PathVariable(name = "idCard") Long idCard);

	@GetMapping("/findByExample")
	AccountCard findByAccoundCardForExample(AccountCard accountCard);
}
