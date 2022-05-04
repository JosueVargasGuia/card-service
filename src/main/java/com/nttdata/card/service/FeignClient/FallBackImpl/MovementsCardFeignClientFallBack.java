package com.nttdata.card.service.FeignClient.FallBackImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nttdata.card.service.FeignClient.MovementsCardFeignClient;
import com.nttdata.card.service.model.MovementsCard;

import lombok.extern.log4j.Log4j2;
@Log4j2
@Component
public class MovementsCardFeignClientFallBack implements MovementsCardFeignClient {
	@Value("${api.movement-card-service.uri}")
	String service;
	public MovementsCard save(MovementsCard movementsCard) {
		log.info("MovementsCardFeignClientFallBack -> " + service + " [" + movementsCard + "]");
		return null;
	}

}