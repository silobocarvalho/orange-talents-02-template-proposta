package br.com.zup.orange.proposal.card;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
import br.com.zup.orange.proposal.card.travel.TravelNotice;
import br.com.zup.orange.proposal.card.travel.TravelNoticeRequest;
import br.com.zup.orange.proposal.externalrequests.card.CardAPIClient;
import br.com.zup.orange.proposal.externalrequests.card.CardBlockRequest;
import br.com.zup.orange.proposal.externalrequests.card.CardBlockResponse;

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
	
	@PersistenceContext
	EntityManager entityManager;

	String urlHost = "http://localhost:8080";

	@Test
	@DisplayName("Associate biometry to a Card")
	void associateBiometryToCard() throws JsonProcessingException, Exception {

		Proposal proposal = new Proposal("12345678909", "sid-test-card@zup.com.br", "Sidartha Carvalho",
				"Rua dos Zuppers, 141", new BigDecimal(2500));
		proposalRepository.save(proposal);

		Card card = new Card("1111222233334444", LocalDateTime.now(), proposal);
		cardRepository.save(card);

		String biometryEncoded = "g6MWsR2M7WHIPPh27WwFfg==";

		CardBiometryRequest cardRequest = new CardBiometryRequest(biometryEncoded);

		mockMvc.perform(MockMvcRequestBuilders.get(urlHost + "/card/" + card.getId())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(cardRequest)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(redirectedUrlPattern("http://*/biometry/*"));
	}

	@Test
	@DisplayName("Associate biometry to a Card with Bigger biometry base64")
	void associateBiometryToCardWithBiggerBiometry() throws JsonProcessingException, Exception {

		Proposal proposal = new Proposal("12345678909", "sid-test-card@zup.com.br", "Sidartha Carvalho",
				"Rua dos Zuppers, 141", new BigDecimal(2500));
		proposalRepository.save(proposal);

		Card card = new Card("1111222233334444", LocalDateTime.now(), proposal);
		cardRepository.save(card);

		String biometryEncoded = "W07Ylqztji7e2Nfm6Ei/pi9CReqEd1e1QpOrAbj+HQwxWeDKXmKDyRyikjweQd0QXVJbiitUmlvyDNRmIpvRWggky83ZAIOggnOvPBImCXYRKzrUuhMGImf1DsOArIoply/y/kleMMa4T3jj9Y2+euwJFucakBIdpr6brqsGO2s=";

		CardBiometryRequest cardRequest = new CardBiometryRequest(biometryEncoded);

		mockMvc.perform(MockMvcRequestBuilders.get(urlHost + "/card/" + card.getId())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(cardRequest)))
				.andExpect(MockMvcResultMatchers.status().isCreated())
				.andExpect(redirectedUrlPattern("http://*/biometry/*"));
	}

	@Test
	@DisplayName("Associate biometry to a Card with empty biometry base64")
	void associateBiometryToCardWithEmptyBiometry() throws JsonProcessingException, Exception {

		Proposal proposal = new Proposal("12345678909", "sid-test-card@zup.com.br", "Sidartha Carvalho",
				"Rua dos Zuppers, 141", new BigDecimal(2500));
		proposalRepository.save(proposal);

		Card card = new Card("1111222233334444", LocalDateTime.now(), proposal);
		cardRepository.save(card);

		String biometryEncoded = "";

		CardBiometryRequest cardRequest = new CardBiometryRequest(biometryEncoded);

		mockMvc.perform(MockMvcRequestBuilders.get(urlHost + "/card/" + card.getId())

				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(cardRequest)))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());

	}

	@Test
	@DisplayName("Associate biometry to a Card with invalid card")
	void associateBiometryToCardWithInvalidCard() throws JsonProcessingException, Exception {

		Proposal proposal = new Proposal("12345678909", "sid-test-card@zup.com.br", "Sidartha Carvalho",
				"Rua dos Zuppers, 141", new BigDecimal(2500));
		proposalRepository.save(proposal);

		// Do not persisted into database
		Card card = new Card("1111222233334444", LocalDateTime.now(), proposal);

		String biometryEncoded = "g6MWsR2M7WHIPPh27WwFfg==";

		CardBiometryRequest cardRequest = new CardBiometryRequest(biometryEncoded);

		mockMvc.perform(MockMvcRequestBuilders.get(urlHost + "/card/" + card.getId())
				.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(cardRequest)))
				.andExpect(MockMvcResultMatchers.status().isNotFound());

	}

	/*
	 * Functionality tested with Postman
	 * 
	 * @Test
	 * 
	 * @DisplayName("Block a card") void blockCard() throws JsonProcessingException,
	 * Exception {
	 * 
	 * Proposal proposal = new Proposal("12345678909", "sid-test-card@zup.com.br",
	 * "Sidartha Carvalho", "Rua dos Zuppers, 141", new BigDecimal(2500));
	 * proposalRepository.save(proposal);
	 * 
	 * Card card = new Card("1111222233334444", LocalDateTime.now(), proposal);
	 * cardRepository.save(card);
	 * 
	 * mockMvc.perform( MockMvcRequestBuilders.get(urlHost + "/card/" + card.getId()
	 * + "/lock") .contentType(MediaType.APPLICATION_JSON)
	 * .header(HttpHeaders.USER_AGENT, "Junit-User-Agent"))
	 * .andExpect(MockMvcResultMatchers.status().isOk());
	 * 
	 * Card cardFromDb = cardRepository.getOne(card.getId());
	 * 
	 * assertEquals(LockStatus.BLOCKED, cardFromDb.getLockStatus()); }
	 */

	@Test
	@DisplayName("Block a already blocked card")
	void blockCardAlreadyBlocked() throws JsonProcessingException, Exception {

		Proposal proposal = new Proposal("12345678909", "sid-test-card@zup.com.br", "Sidartha Carvalho",
				"Rua dos Zuppers, 141", new BigDecimal(2500));
		proposalRepository.save(proposal);

		Card card = new Card("1111222233334444", LocalDateTime.now(), proposal);
		card.lockCard();
		cardRepository.save(card);

		mockMvc.perform(MockMvcRequestBuilders.get(urlHost + "/card/" + card.getId() + "/lock")
				.contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.USER_AGENT, "Junit-User-Agent"))
				.andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
	}

	@Test
	@DisplayName("Block a invalid card")
	void blockCardWithInvalidCardNumber() throws JsonProcessingException, Exception {

		Proposal proposal = new Proposal("12345678909", "sid-test-card@zup.com.br", "Sidartha Carvalho",
				"Rua dos Zuppers, 141", new BigDecimal(2500));
		proposalRepository.save(proposal);

		// Do not persisted into database
		Card card = new Card("1111222233334444", LocalDateTime.now(), proposal);
		card.lockCard();

		mockMvc.perform(MockMvcRequestBuilders.get(urlHost + "/card/" + card.getId() + "/lock")
				.contentType(MediaType.APPLICATION_JSON).header(HttpHeaders.USER_AGENT, "Junit-User-Agent"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

/* Tested with Postman.
 * 
	@Test
	@DisplayName("Associate travel notice with a Card")
	void associateTravelNoticeWithCard() throws JsonProcessingException, Exception {

		Proposal proposal = new Proposal("12345678909", "sid-test-card@zup.com.br", "Sidartha Carvalho",
				"Rua dos Zuppers, 141", new BigDecimal(2500));
		proposalRepository.save(proposal);

		Card card = new Card("1111222233334444", LocalDateTime.now(), proposal);
		card = cardRepository.save(card);

		String travelDestiny = "Quixadá";
		LocalDateTime travelEndDate = LocalDateTime.now().plusDays(1);
		
		TravelNoticeRequest travelNoticeRequest = new TravelNoticeRequest(travelDestiny, travelEndDate);
		
		mockMvc.perform(
				MockMvcRequestBuilders.get(urlHost + "/card/" + card.getId() + "/travelnotice")
		.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(travelNoticeRequest))
		.header(HttpHeaders.USER_AGENT, "Junit-User-Agent"))		
		.andExpect(MockMvcResultMatchers.status().isOk());
		
		assertEquals(travelDestiny, card.getTravelNotice().getDestiny());
		assertEquals(travelEndDate, card.getTravelNotice().getTravelEndDate());
		
		List<TravelNotice> travelNotices = entityManager.createQuery("SELECT tn FROM TravelNotice tn").getResultList();
		assertFalse(travelNotices.isEmpty());
		assertEquals(travelDestiny, travelNotices.get(0).getDestiny());
		
	}
	
 */
	
	@Test
	@DisplayName("Associate travel notice with travel date in the past")
	void associateTravelNoticeWithTravelDateInPast() throws JsonProcessingException, Exception {

		Proposal proposal = new Proposal("12345678909", "sid-test-card@zup.com.br", "Sidartha Carvalho",
				"Rua dos Zuppers, 141", new BigDecimal(2500));
		proposalRepository.save(proposal);

		Card card = new Card("1111222233334444", LocalDateTime.now(), proposal);
		card = cardRepository.save(card);

		String travelDestiny = "Quixadá";
		LocalDateTime travelEndDate = LocalDateTime.now().minusDays(1);
		
		TravelNoticeRequest travelNoticeRequest = new TravelNoticeRequest(travelDestiny, travelEndDate);
		
		mockMvc.perform(
				MockMvcRequestBuilders.get(urlHost + "/card/" + card.getId() + "/travelnotice")
		.contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(travelNoticeRequest))
		.header(HttpHeaders.USER_AGENT, "Junit-User-Agent"))		
		.andExpect(MockMvcResultMatchers.status().isBadRequest());
		
	}
}
