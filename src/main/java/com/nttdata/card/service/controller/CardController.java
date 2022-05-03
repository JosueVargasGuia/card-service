package com.nttdata.card.service.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nttdata.card.service.entity.Card;
import com.nttdata.card.service.model.AccountCard;
import com.nttdata.card.service.sevice.CardService;

import lombok.experimental.var;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@RestController
@RequestMapping("/card")
public class CardController {
	@Autowired
	CardService cardService;

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public Flux<Card> findAll() {
		return cardService.findAll();

	}

	@GetMapping(value = "/{idCard}", produces = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Card>> findById(@PathVariable("idCard") Long idCard) {
		return cardService.findById(idCard).map(_card -> ResponseEntity.ok().body(_card)).onErrorResume(e -> {
			log.error("Error: " + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
		}).defaultIfEmpty(ResponseEntity.noContent().build());
	}

	@PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Card>> save(@RequestBody Card card) {
		return cardService.save(card).map(_card -> ResponseEntity.ok().body(_card)).onErrorResume(e -> {
			log.error("Error:" + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
		});
	}

	@PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Card>> update(@RequestBody Card card) {
		Mono<Card> objCard = cardService.findById(card.getIdCard()).flatMap(_act -> {
			log.info("Update: [new] " + card + " [Old]: " + _act);
			return cardService.update(card);
		});
		return objCard.map(_card -> {
			log.info("Status: " + HttpStatus.OK);
			return ResponseEntity.ok().body(_card);
		}).onErrorResume(e -> {
			log.error("Status: " + HttpStatus.BAD_REQUEST + " Message:  " + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
		}).defaultIfEmpty(ResponseEntity.noContent().build());
	}

	@DeleteMapping(value = "/{idCard}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Void>> delete(@PathVariable("idCard") Long idCard) {
		
		
		return cardService.findById(idCard).flatMap(card -> {
			return cardService.delete(card.getIdCard()).then(Mono.just(ResponseEntity.ok().build()));
		});
	}

	@PostMapping(value = "/registerCard", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Map<String, Object>>> registerCard(@RequestBody Card card) {
		return cardService.registerCard(card).map(_object -> {
			return ResponseEntity.ok().body(_object);
		}).onErrorResume(e -> {
			log.error("Error:" + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
		}).defaultIfEmpty(ResponseEntity.noContent().build());
	}

	@PostMapping(value = "/associateAccountCard", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<ResponseEntity<Map<String, Object>>> associateAccountCard(@RequestBody AccountCard accountCard) {
		return cardService.associateAccountCard(accountCard).map(_object -> {
			return ResponseEntity.ok().body(_object);
		}).onErrorResume(e -> {
			log.error("Error:" + e.getMessage());
			return Mono.just(ResponseEntity.badRequest().build());
		}).defaultIfEmpty(ResponseEntity.noContent().build());
	}
}
