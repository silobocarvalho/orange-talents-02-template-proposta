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

@RestController
@Validated
@RequestMapping("/proposal")
public class ProposalController {

	@Autowired
	ProposalRepository proposalRepository;

	@PostMapping
	@Transactional
	public ResponseEntity<Object> addProposal(@Valid @RequestBody ProposalRequest newRequestProposal,
			UriComponentsBuilder uriComponentsBuilder) {
		
		Proposal newProposal = newRequestProposal.toModel();
		
		if(proposalRepository.existsProposalByDocument(newProposal.getDocument())) {
			//Proposal already exists into database
			return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).build();
		}
		
		//Verify if user has restrictions in his name
		//http://localhost:9999/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/
		
		
		Proposal proposal = proposalRepository.save(newProposal);

		String newProposalURL = uriComponentsBuilder.path("proposal/{id}")
				.buildAndExpand(proposal.getId()).toString();

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(URI.create(newProposalURL));
		return new ResponseEntity<>(headers, HttpStatus.CREATED);

	}
}
