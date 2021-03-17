package br.com.zup.orange.proposal.card;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.zup.orange.proposal.Proposal;

import br.com.zup.orange.proposal.card.travel.TravelNotice;

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
	
	@ManyToOne
	Biometry biometry;
	
	@Enumerated(EnumType.STRING)
	@NotNull
	LockStatus lockStatus;
	
	@ManyToOne
	TravelNotice travelNotice;

	@Deprecated
	public Card() {}
	
	public Card(@NotBlank String cardNumber, @NotNull LocalDateTime releaseTime,
			@NotBlank Proposal proposal) {
		this.cardNumber = cardNumber;
		this.releaseTime = releaseTime;
		this.proposal = proposal;
		this.lockStatus = LockStatus.RELEASED;
	}
	
	public void associateBiometry(Biometry biometry) {
		this.biometry = biometry;
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

	public Biometry getBiometry() {
		return biometry;
	}

	public LockStatus getLockStatus() {
		return lockStatus;
	}
	
	public void lockCard() {
		this.lockStatus = LockStatus.BLOCKED;
	}

	public void associateTravelNotice(TravelNotice travelNotice) {
		this.travelNotice = travelNotice; 
	}

	public TravelNotice getTravelNotice() {
		return travelNotice;
	}
	

}
