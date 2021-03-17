package br.com.zup.orange.proposal.externalrequests.card.travelnotice;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class TravelNoticeExternalRequest {
	@JsonProperty("destino")
	@NotBlank
	private String destiny;
	
	@JsonProperty("validoAte")
	@NotBlank
	private String validthru;

	@Deprecated
	public TravelNoticeExternalRequest() {}
	
	public TravelNoticeExternalRequest(@NotBlank String destiny, @NotBlank String validthru) {
		this.destiny = destiny;
		this.validthru = validthru;
	}

	public String getDestiny() {
		return destiny;
	}

	public String getValidthru() {
		return validthru;
	}
	
	
	
	

}
