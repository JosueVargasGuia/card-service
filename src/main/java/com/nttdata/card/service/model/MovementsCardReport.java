package com.nttdata.card.service.model;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nttdata.card.service.entity.Card;

public class MovementsCardReport {
	Long idMovementCard;
	Long idCard;
	TypeOperation typeOperation;
	Double amount;
	List<MovementCardDetails>movementCardDetails;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private Date creationDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
	private Date dateModified;
	Card card;
}
