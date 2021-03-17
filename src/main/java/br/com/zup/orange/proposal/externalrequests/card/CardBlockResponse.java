package br.com.zup.orange.proposal.externalrequests.card;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.zup.orange.proposal.Proposal;
import br.com.zup.orange.proposal.ProposalRepository;
import br.com.zup.orange.proposal.card.Card;
import br.com.zup.orange.proposal.card.LockStatus;

public class CardBlockResponse {

	@JsonProperty("resultado")
	@NotBlank
	String cardStatus;

	@Deprecated
	public CardBlockResponse() {}
	
	public CardBlockResponse(@NotBlank String cardStatus) {
		this.cardStatus = cardStatus;
	}

	public String getCardStatus() {
		return cardStatus;
	}

	public LockStatus toLockStatus() {
		if(this.cardStatus.equals("BLOQUEADO")) {
			return LockStatus.BLOCKED;
		}
		return LockStatus.RELEASED;
	}

}
