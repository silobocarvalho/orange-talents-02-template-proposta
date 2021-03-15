package br.com.zup.orange.proposal.card;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.zup.orange.proposal.Proposal;
import br.com.zup.orange.proposal.ProposalRepository;
import br.com.zup.orange.proposal.ProposalRequest;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureDataJpa
@Transactional
@ExtendWith(MockitoExtension.class)
public class CardControllerTest {

	@Autowired
	MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;
	
	@Autowired
	ProposalRepository proposalRepository;
	
	@Autowired
	CardRepository cardRepository;
	
	String urlHost = "http://localhost:8080";
	
	@Test
	@DisplayName("Associate biometry to a Card")
	void associateBiometryToCard() throws JsonProcessingException, Exception {

		Proposal proposal = new Proposal("12345678909", "sid-test-card@zup.com.br", "Sidartha Carvalho", "Rua dos Zuppers, 141", new BigDecimal(2500));
		proposalRepository.save(proposal);
		
		Card card = new Card("1111222233334444", LocalDateTime.now(), proposal);
		cardRepository.save(card);
		
		String biometryEncoded = "g6MWsR2M7WHIPPh27WwFfg==";
		
		CardRequest cardRequest = new CardRequest(biometryEncoded);
		
		mockMvc.perform(MockMvcRequestBuilders.get(urlHost + "/card/" + card.getId()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cardRequest)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(redirectedUrlPattern("http://*/biometry/*"));
	}
	
	@Test
	@DisplayName("Associate biometry to a Card with Bigger biometry base64")
	void associateBiometryToCardWithBiggerBiometry() throws JsonProcessingException, Exception {

		Proposal proposal = new Proposal("12345678909", "sid-test-card@zup.com.br", "Sidartha Carvalho", "Rua dos Zuppers, 141", new BigDecimal(2500));
		proposalRepository.save(proposal);
		
		Card card = new Card("1111222233334444", LocalDateTime.now(), proposal);
		cardRepository.save(card);
		
		String biometryEncoded = "W07Ylqztji7e2Nfm6Ei/pi9CReqEd1e1QpOrAbj+HQwxWeDKXmKDyRyikjweQd0QXVJbiitUmlvyDNRmIpvRWggky83ZAIOggnOvPBImCXYRKzrUuhMGImf1DsOArIoply/y/kleMMa4T3jj9Y2+euwJFucakBIdpr6brqsGO2s=";
		
		CardRequest cardRequest = new CardRequest(biometryEncoded);
		
		mockMvc.perform(MockMvcRequestBuilders.get(urlHost + "/card/" + card.getId()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cardRequest)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(redirectedUrlPattern("http://*/biometry/*"));
	}
	
	@Test
	@DisplayName("Associate biometry to a Card with empty biometry base64")
	void associateBiometryToCardWithEmptyBiometry() throws JsonProcessingException, Exception {

		Proposal proposal = new Proposal("12345678909", "sid-test-card@zup.com.br", "Sidartha Carvalho", "Rua dos Zuppers, 141", new BigDecimal(2500));
		proposalRepository.save(proposal);
		
		Card card = new Card("1111222233334444", LocalDateTime.now(), proposal);
		cardRepository.save(card);
		
		String biometryEncoded = "";
		
		CardRequest cardRequest = new CardRequest(biometryEncoded);
		
		mockMvc.perform(MockMvcRequestBuilders.get(urlHost + "/card/" + card.getId()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cardRequest)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	
	}
	
	@Test
	@DisplayName("Associate biometry to a Card with invalid card")
	void associateBiometryToCardWithInvalidCard() throws JsonProcessingException, Exception {

		Proposal proposal = new Proposal("12345678909", "sid-test-card@zup.com.br", "Sidartha Carvalho", "Rua dos Zuppers, 141", new BigDecimal(2500));
		proposalRepository.save(proposal);
		
		// Do not persisted into database
		Card card = new Card("1111222233334444", LocalDateTime.now(), proposal);
		
		String biometryEncoded = "g6MWsR2M7WHIPPh27WwFfg==";
		
		CardRequest cardRequest = new CardRequest(biometryEncoded);
		
		mockMvc.perform(MockMvcRequestBuilders.get(urlHost + "/card/" + card.getId()).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(cardRequest)))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	
	}
}
