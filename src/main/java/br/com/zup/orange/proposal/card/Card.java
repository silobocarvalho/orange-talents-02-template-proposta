package br.com.zup.orange.proposal.card;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.zup.orange.proposal.Proposal;

@Entity
public class Card {

	@Id
	@GeneratedValue
	private UUID id;

	@NotBlank
	String cardNumber;

	@NotNull
	LocalDateTime releaseTime;

	@NotNull
	LocalDateTime createdAt = LocalDateTime.now();

	@NotNull
	@OneToOne(mappedBy = "card")
	Proposal proposal;

	@Deprecated
	public Card() {}
	
	public Card(@NotBlank String cardNumber, @NotNull LocalDateTime releaseTime,
			@NotBlank Proposal proposal) {
		this.cardNumber = cardNumber;
		this.releaseTime = releaseTime;
		this.proposal = proposal;
	}

	public UUID getId() {
		return id;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public LocalDateTime getReleaseTime() {
		return releaseTime;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public Proposal getProposal() {
		return proposal;
	}

}
