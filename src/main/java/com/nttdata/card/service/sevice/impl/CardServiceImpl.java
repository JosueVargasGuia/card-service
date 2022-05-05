package com.nttdata.card.service.sevice.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.nttdata.card.service.FeignClient.AccountCardFeignClient;
import com.nttdata.card.service.FeignClient.BankAccountFeignClient;
import com.nttdata.card.service.FeignClient.BankTransfersFeignClient;
import com.nttdata.card.service.FeignClient.CreditAccountFeignClient;
import com.nttdata.card.service.FeignClient.HolderAccountFeignClient;
import com.nttdata.card.service.FeignClient.MovementAccountFeignClient;
import com.nttdata.card.service.FeignClient.MovementCreditFeignClient;
import com.nttdata.card.service.FeignClient.MovementsCardFeignClient;
import com.nttdata.card.service.FeignClient.SignCustomAccoFeignClient;
import com.nttdata.card.service.FeignClient.TableIdFeignClient;
import com.nttdata.card.service.entity.Card;
import com.nttdata.card.service.entity.CardType;
import com.nttdata.card.service.model.AccountCard;
import com.nttdata.card.service.model.AccountTransfers;
import com.nttdata.card.service.model.BankAccounts;
import com.nttdata.card.service.model.CreditAccount;
import com.nttdata.card.service.model.FinancialOperation;
import com.nttdata.card.service.model.FinancialOperationDetails;
import com.nttdata.card.service.model.HolderAccount;
import com.nttdata.card.service.model.MovementAccount;
import com.nttdata.card.service.model.MovementCardDetails;
import com.nttdata.card.service.model.MovementCredit;
import com.nttdata.card.service.model.MovementsCard;
import com.nttdata.card.service.model.MovementsCardReport;
import com.nttdata.card.service.model.SignatoriesCustomerAccounts;
import com.nttdata.card.service.model.StatusTransfer;
import com.nttdata.card.service.model.Transfers;
import com.nttdata.card.service.model.TypeAccount;
import com.nttdata.card.service.model.TypeMovementAccount;
import com.nttdata.card.service.model.TypeMovementCredit;
import com.nttdata.card.service.model.TypeOperation;
import com.nttdata.card.service.model.TypeTransfer;
import com.nttdata.card.service.repository.CardRepository;
import com.nttdata.card.service.sevice.CardService;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Log4j2
@Service
public class CardServiceImpl implements CardService {
	@Autowired
	CardRepository cardRepository;
	@Autowired
	TableIdFeignClient tableIdFeignClient;
	@Autowired
	BankAccountFeignClient bankAccountFeignClient;
	@Autowired
	CreditAccountFeignClient creditAccountFeignClient;
	@Autowired
	HolderAccountFeignClient holderAccountFeignClient;
	@Autowired
	SignCustomAccoFeignClient signCustomAccoFeignClient;
	@Autowired
	AccountCardFeignClient accountCardFeignClient;
	@Autowired
	MovementAccountFeignClient movementAccountFeignClient;
	@Autowired
	MovementCreditFeignClient movementCreditFeignClient;
	@Autowired
	BankTransfersFeignClient transfersFeignClient;
	@Autowired
	MovementsCardFeignClient movementsCardFeignClient;

	@Override
	public Flux<Card> findAll() {
		return cardRepository.findAll();
	}

	@Override
	public Mono<Card> findById(Long idCard) {
		return cardRepository.findById(idCard);
	}

	@Override
	public Mono<Card> save(Card card) {
		Long count = this.findAll().collect(Collectors.counting()).blockOptional().get();
		Long idCard;
		if (count != null) {
			if (count <= 0) {
				idCard = Long.valueOf(0);
			} else {
				idCard = this.findAll().collect(Collectors.maxBy(Comparator.comparing(Card::getIdCard))).blockOptional()
						.get().get().getIdCard();
			}

		} else {
			idCard = Long.valueOf(0);

		}
		card.setIdCard(idCard + 1);
		card.setCreationDate(Calendar.getInstance().getTime());
		return cardRepository.insert(card);
	}

	@Override
	public Mono<Card> update(Card card) {
		card.setDateModified(Calendar.getInstance().getTime());
		return cardRepository.save(card);
	}

	@Override
	public Mono<Void> delete(Long idCard) {
		return cardRepository.deleteById(idCard);
	}

	/** Registra una tarjeta */
	@Override
	public Mono<Map<String, Object>> registerCard(Card card) {
		Map<String, Object> map = new HashMap<>();
		if (card.getCardType() != null) {
			if (card.getPassword() == null) {
				card.setPassword(numberRandoms(4, false));
			}
			if (card.getCardNumber() == null) {
				card.setCardNumber(numberRandoms(16, true));
			}
			if (card.getCvv() == null) {
				card.setCvv(numberRandoms(3, false));
			}
			return this.save(card).map(objCard -> {
				map.put("Card", objCard);
				return map;
			});
		} else {
			map.put("Card", "Ingrese el tipo de tarjeta[" + CardType.creditCard + "," + CardType.debitCard + "]");
			return Mono.just(map);
		}
		// if (card.getCardType() == CardType.creditCard) {
		// Cuenta de bancarias o productos pasivos
		// BankAccounts bankAccount =
		// bankAccountFeignClient.findById(card.getIdAccount());
		// Cuentas de credito o product activos

		// Buscando titular de la cuenta
		// HolderAccount holderAccount =
		// holderAccountFeignClient.findById(card.getIdHolderAccount());
		// Buscando firma autorisada
		// SignatoriesCustomerAccounts signatoriesCustomerAccounts =
		// signCustomAccoFeignClient
		// .findById(card.getIdSignCustAccount());
		// }
		// CreditAccount creditAccount =
		// creditAccountFeignClient.findById(card.getIdAccount());

		// boolean valid = true;
		// if (bankAccount == null && creditAccount == null) {
		// valid = false;
		// map.put("Account", "La cuenta no existe.");
		// }
		// if (holderAccount == null && signatoriesCustomerAccounts == null) {
		// valid = false;
		// map.put("HolderSignature", "Se requiere una firma autorisada o titular de la
		// cuenta.");
		// }

		// if (valid) {

		// } else {
		// return Mono.empty();
		// }

	}

	String numberRandoms(int cant, boolean isCard) {
		StringBuilder builder = new StringBuilder();
		for (int i = 1; i < cant + (isCard ? 4 : 1); i++) {
			if (i % 5 == 0 && isCard) {
				builder.append("-");
			} else {
				builder.append((int) (Math.random() * 9));
			}

		}
		return builder.toString();
	}

	/**
	 * Los clientes ahora pueden tener tarjetas de débito asociado a sus cuentas
	 * bancarias
	 */
	@Override
	public Mono<Map<String, Object>> associateAccountCard(AccountCard accountCard) {
		Map<String, Object> map = new HashMap<>();
		BankAccounts bankAccount = null;
		if (accountCard.getTypeAccount() == TypeAccount.BankAccounts) {
			// Cuenta de bancarias o productos pasivos
			bankAccount = bankAccountFeignClient.findById(accountCard.getIdAccount());
		}
		CreditAccount creditAccount = null;
		if (accountCard.getTypeAccount() == TypeAccount.CreditAccount) {
			// Cuentas de credito o product activos
			creditAccount = creditAccountFeignClient.findById(accountCard.getIdAccount());
		}
		if (bankAccount == null && creditAccount == null) {
			map.put("Account", "La cuenta no existe.");
			map.put("status", "error");
		} else {
			Long count = Flux.fromIterable(accountCardFeignClient.findByIdCredit(accountCard.getIdCard()))
					.filter(e -> e.getIdAccount() == accountCard.getIdAccount()
							&& e.getTypeAccount() == accountCard.getTypeAccount())
					.collect(Collectors.counting()).blockOptional().orElse(Long.valueOf(0));
			// log.info("count:"+count);
			if (count <= 0) {
				AccountCard accountCardRespose = accountCardFeignClient.save(accountCard);
				if (accountCardRespose == null) {
					map.put("AccountCard", "El servicio de Account-Card no esta disponible.");
					map.put("status", "error");
				} else {
					map.put("status", "success");
				}
			} else {
				map.put("AccountCard", "La tarjeta ya tiene asignada la cuenta ingresada.");
				map.put("status", "error");
			}
		}

		return Mono.just(map);
	}

	/**
	 * Metodo que realiza las operaciones de payment(pago), withdrawal(retiros),
	 * thirdPartyPayment(pagos a terceros --> tranferencias bancarias);
	 */
	@Override
	public Mono<Map<String, Object>> financialOperation(FinancialOperation financialOperation) {
		Map<String, Object> result = new HashMap<String, Object>();
		boolean valid = false;
		if (financialOperation.getAmount() == null || financialOperation.getAmount() <= 0) {
			result.put("Card", "Ingrese un saldo para realizar operacion.");
			valid = false;
		} else if (financialOperation.getTypeOperation() == TypeOperation.payment) {

			if (financialOperation.getAccountPayable().getTypeAccount() == TypeAccount.externalAccount) {
				if (financialOperation.getAccountPayable().getInterbankAccountCode() == null) {
					result.put("Card", "Ingrese el codigo interbancario para realizar esta operacion.");
					valid = false;
				} else {
					valid = true;
				}
			} else if (financialOperation.getAccountPayable().getTypeAccount() == TypeAccount.CreditAccount) {
				if (financialOperation.getAccountPayable().getIdAccount() != null
						&& financialOperation.getAccountPayable().getIdAccount() >= 1) {
					valid = true;

				} else {
					result.put("Card", "Ingrese el codigo de la cuenta");
					valid = false;
				}
			} else {
				result.put("Card", "Operacion invalida");
				valid = false;
			}

		} else {
			valid = true;
		}

		if (valid) {
			Card card = new Card();
			card.setCardNumber(financialOperation.getCardNumber());
			Example<Card> example = Example.of(card);
			financialOperation.setAmountOperation(financialOperation.getAmount());
			financialOperation.setBalanceTake(0.00);
			financialOperation.setOperationDetails(new ArrayList<FinancialOperationDetails>());
			card = cardRepository.findOne(example).blockOptional().orElse(new Card());
			if (card.getIdCard() != null && card.getIdCard() >= 1) {

				List<AccountCard> accountCardsIni = accountCardFeignClient.findByIdCredit(card.getIdCard());
				List<AccountCard> accountCards = accountCardsIni.stream().map(o -> {
					if (o.getIsMainAccount()) {
						/**
						 * estableciendo la cuenta principal con secuencia 0 para consumir su saldo
						 * primero
						 */
						o.setSequence(Long.valueOf(0));
					}
					return o;
				}).collect(Collectors.toList());
				/** Ordenando la prioridad de consumo de su saldo */
				accountCards.sort((ob1, ob2) -> ob1.getSequence().compareTo(ob2.getSequence()));
				/**
				 * Estableciendo cuanto de saldo tomara por cada cuenta asociada a la tarjeta
				 */
				// accountCards.stream().map(acc -> {

				accountCards = Flux.fromIterable(accountCards).map(acc -> {
					log.info("Map:" + acc.toString());
					if (acc.getTypeAccount() == TypeAccount.BankAccounts) {
						Map<String, Object> resultFeing = new HashMap<String, Object>();
						BankAccounts bankAccounts = new BankAccounts();
						bankAccounts.setIdBankAccount(acc.getIdAccount());
						resultFeing = movementAccountFeignClient.balanceInquiry(bankAccounts);
						log.info("BankAccounts:" + resultFeing);
						if (resultFeing.get("status") != "error") {
							Double _saldo = Double.valueOf(resultFeing.get("accountBalance").toString());
							acc.setAccountBalance(_saldo);
						} else {
							acc.setAccountBalance(0.00);
						}
					}
					if (acc.getTypeAccount() == TypeAccount.CreditAccount) {
						Map<String, Object> resultFeing = new HashMap<String, Object>();
						CreditAccount creditAccount = new CreditAccount();
						creditAccount.setIdCreditAccount(acc.getIdAccount());
						resultFeing = movementCreditFeignClient.balanceInquiry(creditAccount);
						log.info("CreditAccount:" + resultFeing);
						if (resultFeing.get("status") != "error") {
							Double _saldo = Double.valueOf(resultFeing.get("creditBalance").toString());
							acc.setAccountBalance(_saldo);
						} else {
							acc.setAccountBalance(0.00);
						}
					}
					FinancialOperationDetails details = new FinancialOperationDetails();
					if (financialOperation.getAmountOperation() >= 1 && acc.getAccountBalance() >= 1) {
						if (acc.getAccountBalance() <= financialOperation.getAmountOperation()) {
							financialOperation
									.setBalanceTake(financialOperation.getBalanceTake() + acc.getAccountBalance());
							details.setBalanceTake(acc.getAccountBalance());
							financialOperation.setAmountOperation(
									financialOperation.getAmountOperation() - acc.getAccountBalance());

							log.info("financialOperation:" + financialOperation.toString());
						} else {
							financialOperation.setBalanceTake(
									financialOperation.getBalanceTake() + financialOperation.getAmountOperation());
							details.setBalanceTake(financialOperation.getAmountOperation());
							financialOperation.setAmountOperation(0.0);

							log.info("financialOperation:" + financialOperation.toString());
						}

						details.setTypeAccount(acc.getTypeAccount());
						details.setIdAccount(acc.getIdAccount());
						financialOperation.getOperationDetails().add(details);
					}
					return acc;
				}).collectList().blockOptional().get();
				log.info("Condicion 1:" + (financialOperation.getAmountOperation().intValue() == 0)
						+ " AmountOperation:" + financialOperation.getAmountOperation());
				log.info("Condicion 2:"
						+ (financialOperation.getAmount().doubleValue() == financialOperation.getBalanceTake()
								.doubleValue())
						+ " Amount:" + financialOperation.getAmount() + " BalanceTake:"
						+ financialOperation.getBalanceTake());

				if (financialOperation.getAmountOperation().intValue() == 0 && financialOperation.getAmount()
						.doubleValue() == financialOperation.getBalanceTake().doubleValue()) {
					List<FinancialOperationDetails> operationDetails = financialOperation.getOperationDetails();
					List<FinancialOperationDetails> opDeResul = Flux.fromIterable(operationDetails).map(ope -> {
						if (financialOperation.getTypeOperation() == TypeOperation.withdrawal
								&& ope.getTypeAccount() == TypeAccount.BankAccounts) {
							/** Realizando retiro desde las cuentas corrientes */
							MovementAccount movementAccount = new MovementAccount();
							movementAccount.setAmount(ope.getBalanceTake());
							movementAccount.setTypeMovementAccount(TypeMovementAccount.withdrawal);
							movementAccount.setIdBankAccount(ope.getIdAccount());
							Map<String, Object> response = movementAccountFeignClient.recordAccount(movementAccount);
							log.info("response 1:" + response);
							ope.setIsTransfer(StatusTransfer.notTransfer);
							ope.setTypeOperacion(TypeAccount.BankAccounts);
							String status = (String) response.get("status");
							if (status != null) {
								if (status.equalsIgnoreCase("success")) {
									ope.setIdMovement(Long.valueOf(response.get("idMovementAccount").toString()));
								} else {
									ope.setIdMovement(Long.valueOf(0));
									response.forEach((key, value) -> {
										if (!key.equalsIgnoreCase("success")) {
											response.put(key, value);
										}
									});
								}
								ope.setStatus(status);
							} else {
								ope.setStatus("error");
							}
						} else if (financialOperation.getTypeOperation() == TypeOperation.withdrawal
								&& ope.getTypeAccount() == TypeAccount.CreditAccount) {
							/** Realizando retiros desde cuentas de credito */
							MovementCredit movementCredit = new MovementCredit();
							movementCredit.setAmount(ope.getBalanceTake());
							movementCredit.setTypeMovementCredit(TypeMovementCredit.charge);
							movementCredit.setIdCreditAccount(ope.getIdAccount());
							Map<String, Object> response = movementCreditFeignClient.recordsMovement(movementCredit);
							log.info("response 2:" + response);
							ope.setIsTransfer(StatusTransfer.notTransfer);
							ope.setTypeOperacion(TypeAccount.CreditAccount);
							String status = (String) response.get("status");
							if (status != null) {
								if (status.equalsIgnoreCase("success")) {
									ope.setIdMovement(Long.valueOf(response.get("idMovementCredit").toString()));

								} else {
									ope.setIdMovement(Long.valueOf(0));
									response.forEach((key, value) -> {
										if (!key.equalsIgnoreCase("success")) {
											response.put(key, value);
										}
									});
								}
								ope.setStatus(status);
							} else {
								ope.setStatus("error");
							}
						}
						if (financialOperation.getTypeOperation() == TypeOperation.payment && financialOperation
								.getAccountPayable().getTypeAccount() == TypeAccount.CreditAccount) {
							/* Tranferencia de una cuenta de credito a credito */
							ope.setIsTransfer(StatusTransfer.isTransfer);
							Transfers transfers = new Transfers();
							transfers.setAmount(ope.getBalanceTake());
							transfers.setTypeTransfer(TypeTransfer.banking);
							ope.setTypeOperacion(TypeAccount.BankTransfers);
							AccountTransfers accountOrigin = new AccountTransfers();
							accountOrigin.setTypeAccount(ope.getTypeAccount());
							accountOrigin.setIdAccount(ope.getIdAccount());

							AccountTransfers accountTarget = new AccountTransfers();
							accountTarget.setTypeAccount(financialOperation.getAccountPayable().getTypeAccount());
							accountTarget.setIdAccount(financialOperation.getAccountPayable().getIdAccount());

							transfers.setOriginAccount(accountOrigin);
							transfers.setTargetAccount(accountTarget);
							Map<String, Object> response = transfersFeignClient.wireTransfer(transfers);
							log.info("response 3:" + response);
							String status = (String) response.get("status");
							if (status != null) {
								if (status.equalsIgnoreCase("success")) {
									ope.setIdMovement(Long.valueOf(response.get("idBankTransfers").toString()));
								} else {
									ope.setIdMovement(Long.valueOf(0));
									response.forEach((key, value) -> {
										if (!key.equalsIgnoreCase("success")) {
											response.put(key, value);
										}
									});
								}
								ope.setStatus(status);
							} else {
								ope.setStatus("error");
							}
						} else if (financialOperation.getTypeOperation() == TypeOperation.payment && financialOperation
								.getAccountPayable().getTypeAccount() == TypeAccount.externalAccount) {
							ope.setIsTransfer(StatusTransfer.isTransfer);
							/* Tranferencia de una cuenta de credito/cuenta bankaria a una cuenta externa */
							ope.setIsTransfer(StatusTransfer.isTransfer);
							ope.setTypeOperacion(TypeAccount.BankTransfers);
							Transfers transfers = new Transfers();
							transfers.setAmount(ope.getBalanceTake());
							transfers.setTypeTransfer(TypeTransfer.interbank);
							transfers.setInterbankAccountCode(
									financialOperation.getAccountPayable().getInterbankAccountCode());
							ope.setInterbankAccountCode(
									financialOperation.getAccountPayable().getInterbankAccountCode());
							AccountTransfers accountOrigin = new AccountTransfers();
							accountOrigin.setTypeAccount(ope.getTypeAccount());
							accountOrigin.setIdAccount(ope.getIdAccount());
							transfers.setOriginAccount(accountOrigin);
							Map<String, Object> response = transfersFeignClient.wireTransfer(transfers);
							log.info("response 4:" + response);
							String status = (String) response.get("status");
							if (status != null) {
								if (status.equalsIgnoreCase("success")) {
									ope.setIdMovement(Long.valueOf(response.get("idBankTransfers").toString()));
								} else {
									ope.setIdMovement(Long.valueOf(0));
									response.forEach((key, value) -> {
										if (!key.equalsIgnoreCase("success")) {
											response.put(key, value);
										}
									});
								}
								ope.setStatus(status);
							} else {
								ope.setStatus("error");
							}
						}

						return ope;
					}).collectList().blockOptional().get();

					Long count = Flux.fromIterable(opDeResul).filter(e -> e.getStatus().equalsIgnoreCase("error"))
							.collect(Collectors.counting()).blockOptional().orElse(Long.valueOf(-1));
					if (count == 0) {

						MovementsCard movementsCard = new MovementsCard();
						movementsCard.setIdCard(card.getIdCard());
						movementsCard.setTypeOperation(financialOperation.getTypeOperation());
						movementsCard.setAmount(financialOperation.getAmount());
						movementsCard.setMovementCardDetails(Flux.fromIterable(opDeResul).map(e -> {
							MovementCardDetails details = new MovementCardDetails();
							details.setTypeAccount(e.getTypeOperacion());
							details.setAmount(e.getBalanceTake());
							details.setIdMovement(e.getIdMovement());
							details.setInterbankAccountCode(e.getInterbankAccountCode());
							return details;
						}).collectList().blockOptional().get());
						MovementsCard movementsCardRespose = movementsCardFeignClient.save(movementsCard);
						result.put("MovementsCard", movementsCardRespose);
						result.put("status", "success");
						result.put("Operacion", financialOperation);
						// result.put("CardList", accountCards);
					} else {
						opDeResul = Flux.fromIterable(opDeResul).filter(e -> e.getStatus().equalsIgnoreCase("success"))
								.map(obj -> {
									log.info("Rollback:" + obj);
									if (obj.getTypeAccount() == TypeAccount.BankAccounts
											&& obj.getIsTransfer() == StatusTransfer.notTransfer) {
										movementAccountFeignClient.delete(obj.getIdMovement());
									} else if (obj.getTypeAccount() == TypeAccount.CreditAccount
											&& obj.getIsTransfer() == StatusTransfer.notTransfer) {
										movementCreditFeignClient.delete(obj.getIdMovement());
									}
									return obj;
								}).collectList().blockOptional().get();

						result.put("status", "error");
						result.put("Operacion", financialOperation);
						// result.put("CardList", accountCards);
					}

				} else {

					result.put("Card", "Saldo insuficiente para realizar la operacion.");
					result.put("status", "error");
					result.put("Operacion", financialOperation);
					// result.put("CardList", accountCards);
				}

			} else {
				result.put("Card", "La tarjeta no existe");
				result.put("status", "error");
			}
		} else {
			result.put("status", "error");
		}
		// log.info(cardRepository.findOne(example).blockOptional().get());

		return Mono.just(result);
	}

	/**
	 * • Implementar un reporte con los últimos 10 movimientos de la tarjeta de
	 * débito y de crédito.
	 */
	@Override
	public Flux<MovementsCard> lastTenReport(Card card) {
		return Flux.merge(Flux.fromIterable(movementsCardFeignClient.findAll()).map(e -> {
			e.setCard(this.findById(e.getIdCard()).blockOptional().orElse(new Card()));
			log.info("Find:" + e.toString());
			return e;
		}).filter(obj -> obj.getCard().getCardType() == CardType.creditCard)
				.sort((o1, o2) -> o2.getIdMovementCard().compareTo(o1.getIdMovementCard())).take(10)
		// .collectList()
		// .blockOptional().get().forEach(e-> log.info("creditCard:"+e.toString()))
				, Flux.fromIterable(movementsCardFeignClient.findAll()).map(e -> {
					e.setCard(this.findById(e.getIdCard()).blockOptional().orElse(new Card()));
					log.info("Find:" + e.toString());
					return e;
				}).filter(obj -> obj.getCard().getCardType() == CardType.debitCard)
						.sort((o1, o2) -> o2.getIdMovementCard().compareTo(o1.getIdMovementCard())).take(10)
		// .collectList()
		// .blockOptional().get().forEach(e-> log.info("debitCard:"+e.toString()))
		);

	}

}
