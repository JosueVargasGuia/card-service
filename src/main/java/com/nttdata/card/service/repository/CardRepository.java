package com.nttdata.card.service.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import com.nttdata.card.service.entity.Card;

@Repository
public interface CardRepository extends ReactiveMongoRepository<Card, Long> {

}
