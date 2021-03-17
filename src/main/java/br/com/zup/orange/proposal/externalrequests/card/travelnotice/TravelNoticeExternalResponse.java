package br.com.zup.orange.proposal.externalrequests.card.travelnotice;


import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.zup.orange.proposal.card.travel.TravelStatus;

public class TravelNoticeExternalResponse {

	@JsonProperty("resultado")
	@NotBlank
	String travelNoticeStatus;

	@Deprecated
	public TravelNoticeExternalResponse() {};
	
	public TravelNoticeExternalResponse(@NotBlank String travelNoticeStatus) {
		this.travelNoticeStatus = travelNoticeStatus;
	}

	public String getTravelNoticeStatus() {
		return travelNoticeStatus;
	}

	public TravelStatus validateResult() {
		if(this.travelNoticeStatus.equals("CRIADO")) {
			return TravelStatus.OK;
		}else {
			return TravelStatus.NOT_AUTHORIZED;
		}
	}
}
