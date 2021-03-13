package br.com.zup.orange.proposal;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@Transactional
@ExtendWith(MockitoExtension.class)
class ProposalControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	ProposalRepository proposalRepository;

	String urlHost = "http://localhost:8080";

	@Test
	@DisplayName("Create a new Proposal for CPF client")
	void createNewProposalForCPF() throws JsonProcessingException, Exception {

		ProposalRequest newProposal = new ProposalRequest("12345678909", "sid@zup.com.br", "Sidartha Carvalho",
				"Rua Joao Maria, 147", new BigDecimal(2500));

		mockMvc.perform(MockMvcRequestBuilders.post(urlHost + "/proposal").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newProposal)))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	@DisplayName("Create a new Proposal for CNPJ client")
	void createNewProposalForCNPJ() throws JsonProcessingException, Exception {

		ProposalRequest newProposal = new ProposalRequest("01367958000340", "sid@zup.com.br", "Sidartha Carvalho",
				"Rua Joao Maria, 147", new BigDecimal(2500));

		mockMvc.perform(MockMvcRequestBuilders.post(urlHost + "/proposal").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newProposal)))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	@DisplayName("Proposal creation returning ID into Header")
	void createNewProposalReturnProposalIdIntoHeader() throws JsonProcessingException, Exception {

		ProposalRequest newProposal = new ProposalRequest("12345678909", "sid@zup.com.br", "Sidartha Carvalho",
				"Rua Joao Maria, 147", new BigDecimal(2500));

		mockMvc.perform(MockMvcRequestBuilders.post(urlHost + "/proposal").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newProposal)))
				.andExpect(redirectedUrlPattern("http://*/proposal/*"));
	}

	@Test
	@DisplayName("Create a new Proposal with Empty Name")
	void createNewProposalWithEmptyName() throws JsonProcessingException, Exception {

		ProposalRequest newProposal = new ProposalRequest("12345678909", "sid@zup.com.br", "", "Rua Joao Maria, 147",
				new BigDecimal(2500));

		mockMvc.perform(MockMvcRequestBuilders.post(urlHost + "/proposal").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newProposal)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	@DisplayName("Create a new Proposal with negative salary")
	void createNewProposalWithNegativeSalary() throws JsonProcessingException, Exception {

		ProposalRequest newProposal = new ProposalRequest("12345678909", "sid@zup.com.br", "Sidartha Carvalho",
				"Rua Joao Maria, 147", new BigDecimal(-1));

		mockMvc.perform(MockMvcRequestBuilders.post(urlHost + "/proposal").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newProposal)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	@DisplayName("Create a new Proposal with CPF missing numbers")
	void createNewProposalWithCpfMissingNumbers() throws JsonProcessingException, Exception {

		ProposalRequest newProposal = new ProposalRequest("123456789", "sid@zup.com.br", "Sidartha Carvalho",
				"Rua Joao Maria, 147", new BigDecimal(2500));

		mockMvc.perform(MockMvcRequestBuilders.post(urlHost + "/proposal").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newProposal)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	@DisplayName("Create a new Proposal with CNPJ missing numbers")
	void createNewProposalWithCnpjMissingNumbers() throws JsonProcessingException, Exception {

		ProposalRequest newProposal = new ProposalRequest("01.367.958/003-40", "sid@zup.com.br", "Sidartha Carvalho",
				"Rua Joao Maria, 147", new BigDecimal(2500));

		mockMvc.perform(MockMvcRequestBuilders.post(urlHost + "/proposal").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newProposal)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

	@Test
	@DisplayName("Create duplicated Proposal")
	void createDuplicatedProposal() throws JsonProcessingException, Exception {

		ProposalRequest newProposal = new ProposalRequest("12345678909", "sid@zup.com.br", "Sidartha Carvalho",
				"Rua Joao Maria, 147", new BigDecimal(2500));

		// Save into database
		proposalRepository.save(newProposal.toModel());

		// Send request to create the same proposal
		mockMvc.perform(MockMvcRequestBuilders.post(urlHost + "/proposal").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newProposal)))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
	}

	@Test
	@DisplayName("Create a proposal and verify if it is updated by Financial Analysis using external API - ELEGÍVEL")
	void verifyIfProposalIsUpdatedWithFinancialAnalysisElegible() throws JsonProcessingException, Exception {

		ProposalRequest newProposal = new ProposalRequest("12345678909", "sid@zup.com.br", "Sidartha Carvalho",
				"Rua Joao Maria, 147", new BigDecimal(2500));

		mockMvc.perform(MockMvcRequestBuilders.post(urlHost + "/proposal").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newProposal)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		Proposal proposalFromDb = proposalRepository.findByDocument(newProposal.getDocument());

		assertEquals(ProposalStatus.ELEGIVEL, proposalFromDb.getStatus());
	}

	@Test
	@DisplayName("Create a proposal and verify if it is updated by Financial Analysis using external API - NÃO-ELEGÍVEL")
	void verifyIfProposalIsUpdatedWithFinancialAnalysisNonElegible() throws JsonProcessingException, Exception {

		// CPF started with 3 is NÃO ELEGIVEL
		ProposalRequest newProposal = new ProposalRequest("33192040092", "sid@zup.com.br", "Sidartha Carvalho",
				"Rua Joao Maria, 147", new BigDecimal(2500));

		mockMvc.perform(MockMvcRequestBuilders.post(urlHost + "/proposal").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newProposal)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		Proposal proposalFromDb = proposalRepository.findByDocument(newProposal.getDocument());

		assertEquals(ProposalStatus.NAO_ELEGIVEL, proposalFromDb.getStatus());
	}

	@Test
	@DisplayName("Should create some proposals and associate a card number to a proposal")
	void AssociateCardNumberAndProposal() throws JsonProcessingException, Exception {

		// Should insert some proposals into database

		Proposal proposal1 = new Proposal("12345678909", "email1@zup.com.br", "Zup 1", "Rua dos Zuppers, 123",
				new BigDecimal(2500));
		Proposal proposal2 = new Proposal("08273199088", "email2@zup.com.br", "Zup 2", "Rua dos Zuppers, 321",
				new BigDecimal(3000));
		Proposal proposal3 = new Proposal("81172699020", "email3@zup.com.br", "Zup 3", "Rua dos Zuppers, 321",
				new BigDecimal(3000));
		Proposal proposal4 = new Proposal("99090915001", "email4@zup.com.br", "Zup 4", "Rua dos Zuppers, 321",
				new BigDecimal(3000));
		Proposal proposal5 = new Proposal("65642116000170", "email-pj-1@zup.com.br", "Zup PJ 1",
				"Rua dos Zuppers Ricos, 123", new BigDecimal(30000));
		Proposal proposal6 = new Proposal("86875458000100", "email-pj-2@zup.com.br", "Zup PJ 2",
				"Rua dos Zuppers Ricos, 321", new BigDecimal(40000));

		List<Proposal> proposals = new ArrayList<>();
		proposals.add(proposal1);
		proposals.add(proposal2);
		proposals.add(proposal3);
		proposals.add(proposal4);
		proposals.add(proposal5);
		proposals.add(proposal6);

		proposalRepository.saveAll(proposals);

		ProposalRequest newProposal = new ProposalRequest("33192040092", "sid@zup.com.br", "Sidartha Carvalho",
				"Rua Joao Maria, 147", new BigDecimal(2500));

		mockMvc.perform(MockMvcRequestBuilders.post(urlHost + "/proposal").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newProposal)))
				.andExpect(MockMvcResultMatchers.status().isCreated());

		// 7 insertions into database 6 + 1
		List<Proposal> proposalsFromDb = proposalRepository.findAll();
		assertEquals(7, proposalsFromDb.size());

		for (Proposal proposal : proposals) {
			if (proposal.getStatus() != null && proposal.getStatus().equals(ProposalStatus.ELEGIVEL))
				assertNotNull(proposal.getCard());
		}

		// only one is non elegible for card
		Proposal nonElegibleProposal = proposalRepository.findByDocument(newProposal.getDocument());
		assertNull(nonElegibleProposal.getCard());

	}

	@Test
	@DisplayName("Get a proposal using proposal Id")
	void getProposal() throws JsonProcessingException, Exception {

		Proposal proposal = new Proposal("12345678909", "email1@zup.com.br", "Zup 1", "Rua dos Zuppers, 123",
				new BigDecimal(2500));

		Proposal proposalFromDb = proposalRepository.save(proposal);

		String proposalId = proposalFromDb.getId().toString();

		ResultActions result = mockMvc.perform(
				MockMvcRequestBuilders.get(urlHost + "/proposal/" + proposalId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk());

		ProposalResponse proposalResponse = objectMapper
				.readValue(result.andReturn().getResponse().getContentAsString(), ProposalResponse.class);

		System.out.println(proposalResponse);

		assertAll(() -> assertEquals(proposalResponse.getId().toString(), proposalFromDb.getId().toString()),
				() -> assertEquals(proposalResponse.getName(), proposalFromDb.getName()));
	}
	
	@Test
	@DisplayName("Get a proposal using nonexistent proposal Id")
	void getProposalUsingNonexistentProposalId() throws JsonProcessingException, Exception {

		String proposalId = "fa3a7ea2-2056-4248-b564-2e397a560b97";

		mockMvc.perform(
				MockMvcRequestBuilders.get(urlHost + "/proposal/" + proposalId).contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

}
