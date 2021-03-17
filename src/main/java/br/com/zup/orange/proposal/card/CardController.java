package br.com.zup.orange.proposal.card;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.core.support.RepositoryFactoryBeanSupport;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zup.orange.proposal.ProposalController;
import br.com.zup.orange.proposal.ProposalRepository;
import br.com.zup.orange.proposal.card.travel.TravelNoticeRequest;
import br.com.zup.orange.proposal.card.travel.TravelStatus;
import br.com.zup.orange.proposal.card.travel.TravelNotice;
import br.com.zup.orange.proposal.externalrequests.card.CardAPIClient;
import br.com.zup.orange.proposal.externalrequests.card.CardBlockRequest;
import br.com.zup.orange.proposal.externalrequests.card.CardBlockResponse;
import br.com.zup.orange.proposal.externalrequests.card.CardResponse;
import br.com.zup.orange.proposal.externalrequests.card.travelnotice.TravelNoticeExternalRequest;
import br.com.zup.orange.proposal.externalrequests.card.travelnotice.TravelNoticeExternalResponse;
import br.com.zup.orange.proposal.externalrequests.financialanalysis.ClientFinancialAnalysis;
import feign.FeignException;

@RestController
@Validated
@RequestMapping("/card")
@Component
public class CardController {

	private final Logger logger = LoggerFactory.getLogger(ProposalController.class);

	@Autowired
	CardRepository cardRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	CardAPIClient cardAPI;

	@GetMapping("/{cardId}")
	@Transactional
	public ResponseEntity<Object> getProposal(@Valid @NotEmpty @PathVariable("cardId") String cardId,
			@Valid @RequestBody CardBiometryRequest cardRequest, UriComponentsBuilder uriComponentsBuilder)
			throws URISyntaxException {

		UUID cardIdUUID = null;
		try {
			cardIdUUID = UUID.fromString(cardId);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.notFound().build();
		}

		if (cardIdUUID == null) {
			return ResponseEntity.notFound().build();
		}

		Optional<Card> cardOptional = cardRepository.findById(UUID.fromString(cardId));

		if (cardOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Card cardFromDb = cardOptional.get();

		// It's necessary to validate if is a valid base64 String.

		byte[] biometryBytesDecoded = Base64.getDecoder().decode(cardRequest.getBiometryEncoded());
		Biometry biometry = new Biometry(biometryBytesDecoded);

		cardFromDb.associateBiometry(biometry);

		cardRepository.save(cardFromDb);

		URI uri = new URI(
				uriComponentsBuilder.path("biometry/{id}").buildAndExpand(cardFromDb.getBiometry().getId()).toString());
		return ResponseEntity.created(uri).build();
	}

	@GetMapping("/{cardId}/lock")
	@Transactional
	public ResponseEntity<Object> performCardLock(@Valid @NotEmpty @PathVariable("cardId") String cardId,
			@RequestHeader(HttpHeaders.USER_AGENT) String userAgent, HttpServletRequest httpServletRequest) {

		try {
			UUID.fromString(cardId);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}

		Optional<Card> cardOptional = cardRepository.findById(UUID.fromString(cardId));

		if (cardOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		Card cardFromDb = cardOptional.get();

		if (cardFromDb.getLockStatus().equals(LockStatus.BLOCKED)) {
			return ResponseEntity.unprocessableEntity().build();
		}

		LockStatus cardLockStatus = blockCardExternalRequestInfo(cardAPI, cardFromDb);

		if (cardLockStatus.equals(LockStatus.BLOCKED)) {
			cardFromDb.lockCard();
			entityManager.persist(cardFromDb);
			CardLock cardLock = new CardLock(cardFromDb, userAgent, httpServletRequest.getRemoteAddr());
			entityManager.persist(cardLock);

			logger.info("Card with blocked status and CardLock entity persisted");
		} else if (cardLockStatus.equals(LockStatus.RELEASED)) {
			return ResponseEntity.unprocessableEntity().build();
		}

		return ResponseEntity.ok().build();
	}

	@Transactional
	public LockStatus blockCardExternalRequestInfo(CardAPIClient cardAPI, Card cardFromDb) {

		CardBlockResponse cardBlockResponse = cardAPI.blockCard(cardFromDb.getCardNumber(),
				new CardBlockRequest("proposalSystem"));

		logger.info("== Card Controller blockCardExternalRequestInfo() ==");
		logger.info("Status returned from External System: " + cardBlockResponse.getCardStatus());

		try {
			if (cardBlockResponse.getCardStatus().equals("BLOQUEADO")) {
				return LockStatus.BLOCKED;
			}
		} catch (FeignException fe) {
			logger.info("== Card Controller blockCardExternalRequestInfo() ==");
			logger.info(fe.getMessage());
			return LockStatus.RELEASED;
		}
		return LockStatus.RELEASED;
	}

	@PostMapping("/{cardId}/travelnotice")
	@Transactional
	public ResponseEntity<Object> TravelNotice(@Valid @NotEmpty @PathVariable("cardId") String cardId,
			@Valid @RequestBody TravelNoticeRequest travelRequest,
			@RequestHeader(HttpHeaders.USER_AGENT) String userAgent, HttpServletRequest httpServletRequest) {

		try {
			UUID.fromString(cardId);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		}

		Optional<Card> cardOptional = cardRepository.findById(UUID.fromString(cardId));

		if (cardOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		TravelNotice travelNotice = travelRequest.toModel(userAgent, httpServletRequest.getRemoteAddr());
		Card cardFromDb = cardOptional.get();

		TravelStatus travelStatus = travelNoticeExternalRequest(cardAPI, cardFromDb, travelNotice);

		if (travelStatus.equals(TravelStatus.NOT_AUTHORIZED)) {
			return ResponseEntity.badRequest().build();
		}

		cardFromDb.associateTravelNotice(travelNotice);
		cardRepository.save(cardFromDb);

		entityManager.persist(travelNotice);

		logger.info("Card Controller TravelNotice() - Card updated and TravelNotice persisted");
		logger.info("TravelNotice Destiny: " + travelNotice.getDestiny());
		logger.info("TravelNotice End Date: " + travelNotice.getTravelEndDate());

		return ResponseEntity.ok().build();
	}

	@Transactional
	public TravelStatus travelNoticeExternalRequest(CardAPIClient cardAPI, Card cardFromDb, TravelNotice travelNotice) {

		TravelNoticeExternalResponse travelNoticeResponse = null;
		try {
			travelNoticeResponse = cardAPI.travelNotice(cardFromDb.getCardNumber(),
					travelNotice.toRequest());
		} catch (FeignException fe) {
			logger.info("== Card Controller travelNoticeExternalRequest() ==");
			logger.info(fe.getMessage());
			return TravelStatus.NOT_AUTHORIZED;
		}

		TravelStatus travelNoticeStatus = travelNoticeResponse.validateResult();

		logger.info("== Card Controller travelNoticeExternalRequest() ==");
		logger.info("Travel Notice Status from External System: "
				+ travelNoticeResponse.getTravelNoticeStatus().toString());
		logger.info("Travel Notice Status: " + travelNoticeStatus.toString());

		return travelNoticeStatus;
	}

}
