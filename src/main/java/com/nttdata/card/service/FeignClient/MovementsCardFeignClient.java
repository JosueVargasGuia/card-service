package com.nttdata.card.service.FeignClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.nttdata.card.service.FeignClient.FallBackImpl.MovementsCardFeignClientFallBack;
import com.nttdata.card.service.model.MovementsCard;

 
 

@FeignClient(name="${api.movement-card-service.uri}"
,fallback = MovementsCardFeignClientFallBack.class)
public interface MovementsCardFeignClient {
	
	@GetMapping()
	List<MovementsCard> findAll();
	@PostMapping
	MovementsCard save(@RequestBody MovementsCard movementsCard);
}
