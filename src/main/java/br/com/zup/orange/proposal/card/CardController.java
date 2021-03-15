package br.com.zup.orange.proposal.card;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zup.orange.proposal.externalrequests.card.CardAssociate;
import br.com.zup.orange.proposal.externalrequests.financialanalysis.ClientFinancialAnalysis;

@RestController
@Validated
@RequestMapping("/card")
@Component
public class CardController {

	@Autowired
	CardRepository cardRepository;

	@GetMapping("/{cardId}")
	@Transactional
	public ResponseEntity<Object> getProposal(@Valid @NotEmpty @PathVariable("cardId") String cardId,
			@Valid @RequestBody CardRequest cardRequest, UriComponentsBuilder uriComponentsBuilder)
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

}
