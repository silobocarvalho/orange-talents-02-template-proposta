package br.com.zup.orange.proposal;

import java.net.URI;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.tags.RequestContextAwareTag;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.zup.orange.proposal.externalrequests.financialanalysis.ClientFinancialAnalysis;
import br.com.zup.orange.proposal.externalrequests.financialanalysis.ClientFinancialAnalysisRequest;
import br.com.zup.orange.proposal.externalrequests.financialanalysis.ClientFinancialAnalysisResponse;

@RestController
@Validated
@RequestMapping("/proposal")
public class ProposalController {

	@Autowired
	ProposalRepository proposalRepository;
	
	@Autowired
	ClientFinancialAnalysis clientFinancialAnalysis;

	@PostMapping
	@Transactional
	public ResponseEntity<Object> addProposal(@Valid @RequestBody ProposalRequest newRequestProposal,
			UriComponentsBuilder uriComponentsBuilder) {
		
		Proposal newProposal = newRequestProposal.toModel();
		
		if(proposalRepository.existsProposalByDocument(newProposal.getDocument())) {
			//Proposal already exists into database
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
		}
		
		Proposal proposal = proposalRepository.save(newProposal);

		//Check if user has restriction in his name using external API
		proposal.updateFinancialAnalysis(clientFinancialAnalysis);

		String newProposalURL = uriComponentsBuilder.path("proposal/{id}")
				.buildAndExpand(proposal.getId()).toString();

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(newProposalURL));
		return new ResponseEntity<>(headers, HttpStatus.CREATED);

	}
}
