package br.com.zup.orange.proposal.card.travel;

import java.time.LocalDateTime;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

@Validated
public class TravelNoticeRequest {

	@NotBlank
	String destiny;
	
	@NotNull
	@FutureOrPresent
	LocalDateTime travelEndDate;

	public TravelNoticeRequest(@NotBlank String destiny, @NotNull @FutureOrPresent LocalDateTime travelEndDate) {
		this.destiny = destiny;
		this.travelEndDate = travelEndDate;
	}

	public String getDestiny() {
		return destiny;
	}

	public LocalDateTime getTravelEndDate() {
		return travelEndDate;
	}

	public TravelNotice toModel(String userAgent, String requestIP ) {
		
		return new TravelNotice(this.destiny, this.travelEndDate, userAgent, requestIP);
		
	}
	
	
}
