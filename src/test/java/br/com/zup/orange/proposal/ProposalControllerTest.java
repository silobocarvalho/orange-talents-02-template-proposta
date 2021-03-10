package br.com.zup.orange.proposal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
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
		
		//Save into database
		proposalRepository.save(newProposal.toModel());
		
		//Send request to create the same proposal
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

		//CPF started with 3 is NÃO ELEGIVEL
		ProposalRequest newProposal = new ProposalRequest("33192040092", "sid@zup.com.br", "Sidartha Carvalho",
				"Rua Joao Maria, 147", new BigDecimal(2500));	
		
		mockMvc.perform(MockMvcRequestBuilders.post(urlHost + "/proposal").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(newProposal)))
				.andExpect(MockMvcResultMatchers.status().isCreated());
		
		Proposal proposalFromDb = proposalRepository.findByDocument(newProposal.getDocument());
		
		assertEquals(ProposalStatus.NAO_ELEGIVEL, proposalFromDb.getStatus());
	}

}
