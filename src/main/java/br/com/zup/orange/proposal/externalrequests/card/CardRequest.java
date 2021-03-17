package br.com.zup.orange.proposal.externalrequests.card;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardRequest {

	@JsonProperty("id")
	@NotBlank
	String CardId;

	public CardRequest(@NotBlank String cardId) {
		CardId = cardId;
	}

	public String getCardId() {
		return CardId;
	}

	
}
