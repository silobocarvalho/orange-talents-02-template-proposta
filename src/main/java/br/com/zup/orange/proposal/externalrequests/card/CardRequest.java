package br.com.zup.orange.proposal.externalrequests.card;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardRequest {

	@JsonProperty("idProposta")
	@NotBlank
	String proposalId;

	public CardRequest(String proposalId) {
		this.proposalId = proposalId;
	}


	public String getProposalId() {
		return proposalId;
	}
}
