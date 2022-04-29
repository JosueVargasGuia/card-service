package com.nttdata.card.service.sevice;

import java.util.Map;

import com.nttdata.card.service.entity.Card;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CardService {
	Flux<Card> findAll();

	Mono<Card> findById(Long idCard);

	Mono<Card> save(Card card);

	Mono<Card> update(Card card);

	Mono<Void> delete(Long idCard);
	Mono<Map<String, Object>> registerCard(Card card);
}
