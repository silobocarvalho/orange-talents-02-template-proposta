package br.com.zup.orange.proposal.card.travel;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.zup.orange.proposal.externalrequests.card.travelnotice.TravelNoticeExternalRequest;

@Entity
public class TravelNotice {
	
	@Id
	@GeneratedValue
	private UUID id;
	
	@NotBlank
	String destiny;
	
	@NotNull
	@FutureOrPresent
	LocalDateTime travelEndDate;
	
	@NotNull
	LocalDateTime createdAt = LocalDateTime.now();
	
	@NotBlank
	String requestUserAgent;
	
	@NotBlank
	String requestUserIP;

	@Deprecated
	public TravelNotice() {}
	
	public TravelNotice(@NotBlank String destiny, @NotNull @FutureOrPresent LocalDateTime travelEndDate,
			@NotBlank String requestUserAgent, @NotBlank String requestUserIP) {
		this.destiny = destiny;
		this.travelEndDate = travelEndDate;
		this.requestUserAgent = requestUserAgent;
		this.requestUserIP = requestUserIP;
	}

	public UUID getId() {
		return id;
	}

	public String getDestiny() {
		return destiny;
	}

	public LocalDateTime getTravelEndDate() {
		return travelEndDate;
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

	public TravelNoticeExternalRequest toRequest() {
		
		return new TravelNoticeExternalRequest(this.destiny, this.getTravelEndDate().toString());
	}
	
	
}
