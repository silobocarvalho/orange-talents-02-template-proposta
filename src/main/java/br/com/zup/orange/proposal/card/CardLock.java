package br.com.zup.orange.proposal.card;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
public class CardLock {

	@Id
	@GeneratedValue
	private UUID id;
	
	@NotNull
	@OneToOne
	Card card;
	
	@NotNull
	LocalDateTime createdAt = LocalDateTime.now();
	
	@NotBlank
	String requestUserAgent;
	
	@NotBlank
	String requestUserIP;

	public CardLock(@NotNull Card card, @NotBlank String requestUserAgent,
			@NotBlank String requestUserIP) {
		this.card = card;
		this.requestUserAgent = requestUserAgent;
		this.requestUserIP = requestUserIP;
	}

	public UUID getId() {
		return id;
	}

	public Card getCard() {
		return card;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public String getRequestUserAgent() {
		return requestUserAgent;
	}

	public String getRequestUserIP() {
		return requestUserIP;
	}
	
	
	
}
