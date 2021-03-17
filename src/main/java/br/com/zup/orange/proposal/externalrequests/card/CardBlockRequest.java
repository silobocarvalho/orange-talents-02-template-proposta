package br.com.zup.orange.proposal.externalrequests.card;


import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardBlockRequest {

	@JsonProperty("sistemaResponsavel")
	@NotBlank
	private String responsibleSystem;

	@Deprecated
	public CardBlockRequest() {}
	
	public CardBlockRequest(@NotBlank String responsibleSystem) {
		this.responsibleSystem = responsibleSystem;
	}

	public String getResponsibleSystem() {
		return responsibleSystem;
	}


	
}
