package br.com.zup.orange.proposal.externalrequests.card;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.zup.orange.proposal.Proposal;
import br.com.zup.orange.proposal.ProposalRepository;
import br.com.zup.orange.proposal.card.Card;


public class CardResponse {

	@JsonProperty("idProposta")
	@NotBlank
	String proposalId;
	
	@JsonProperty("id")
	@NotBlank
	String cardNumber;

	@JsonProperty("emitidoEm")
	@NotNull
	LocalDateTime releaseTime;
	

	@Deprecated
	public CardResponse() {}

	

	public CardResponse(@NotBlank String proposalId, @NotBlank String cardNumber, @NotNull LocalDateTime releaseTime) {
		this.proposalId = proposalId;
		this.cardNumber = cardNumber;
		this.releaseTime = releaseTime;
	}



	public String getCardNumber() {
		return cardNumber;
	}

	public LocalDateTime getReleaseTime() {
		return releaseTime;
	}

	public String getProposalId() {
		return proposalId;
	}

	@Override
	public String toString() {
		return "CardResponse [cardNumber=" + cardNumber + ", releaseTime=" + releaseTime + ", proposalId=" + proposalId
				+ "]";
	}
	
	public Card toModel(ProposalRepository proposalRepository, UUID proposalId) {
		
		if(this.proposalId == null) {this.proposalId = proposalId.toString();}
		
		Optional<Proposal> proposalOptional = proposalRepository.findById(UUID.fromString(this.proposalId));
		
		if(proposalOptional != null) {
			return new Card(this.cardNumber, this.releaseTime, proposalOptional.get());
		}
		return null;
	}
	
	

}
