package br.com.zup.orange.proposal;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;
import javax.validation.Valid;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zup.orange.proposal.externalrequests.card.CardAPIClient;
import br.com.zup.orange.proposal.externalrequests.financialanalysis.ClientFinancialAnalysis;
import io.opentracing.Span;
import io.opentracing.Tracer;

@RestController
@Validated
@RequestMapping("/proposal")
@Component
public class ProposalController {

	private final Logger logger = LoggerFactory.getLogger(ProposalController.class);
	
	@Autowired
	ProposalRepository proposalRepository;
	
	@Autowired
	ClientFinancialAnalysis clientFinancialAnalysis;
	
	@Autowired
	CardAPIClient cardAssociate;
	
	private final Tracer tracer;

	public ProposalController(Tracer tracer) {
		 this.tracer = tracer;
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<Object> addProposal(@Valid @RequestBody ProposalRequest newRequestProposal,
			UriComponentsBuilder uriComponentsBuilder) {
		
		Span activeSpan = tracer.activeSpan();
		activeSpan.setTag("user.email", newRequestProposal.getEmail());
		/*
		 * Used as Tag, but for another purpose :)
		Span activeSpan = tracer.activeSpan();
		String userEmail = activeSpan.getBaggageItem("user.email");
		activeSpan.setBaggageItem("user.email", userEmail);
		*/

		
		Proposal newProposal = newRequestProposal.toModel();
		
		if(proposalRepository.existsProposalByDocument(newProposal.getDocument())) {
			//Proposal already exists into database
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
		}
		
		Proposal proposal = proposalRepository.save(newProposal);

		//Check if user has restriction in his name using external API
		proposal.updateFinancialAnalysis(clientFinancialAnalysis);
		System.out.println("\n\n" + proposal.getId() + "\n\n");
		String newProposalURL = uriComponentsBuilder.path("proposal/{id}")
				.buildAndExpand(proposal.getId()).toString();

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(newProposalURL));
		return new ResponseEntity<>(headers, HttpStatus.CREATED);

	}
	
	@Async
	@Scheduled(initialDelay = 2000, fixedRate = 5000)
	@Transactional
	public void associateCardNumberToProposal() {
		
		List<Proposal> proposals = proposalRepository.findByStatusAndCardIsNullOrderByCreatedAtAsc(ProposalStatus.ELEGIVEL);
		
		for (Proposal proposal : proposals) {
			proposal.updateCardInfo(cardAssociate, proposalRepository);
			logger.info("== Proposal update from @Scheduled method ==");
			logger.info("Proposal ID: " + proposal.getId());
			logger.info("Card ID: " + proposal.getCard().getId());
			logger.info("Card Number: " + proposal.getCard().getCardNumber());
		}
	}
	
	
	@GetMapping("/{proposalId}")
	@Transactional
	public ResponseEntity<Object> getProposal(@Valid @NotEmpty @PathVariable("proposalId") String proposalId) {
	
		Optional<Proposal> proposalOptional = proposalRepository.findById(UUID.fromString(proposalId));
		
		if(proposalOptional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok().body(proposalOptional.get().toResponse());
		
	}

}
