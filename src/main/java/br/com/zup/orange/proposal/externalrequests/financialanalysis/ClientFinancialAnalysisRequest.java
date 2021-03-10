package br.com.zup.orange.proposal.externalrequests.financialanalysis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientFinancialAnalysisRequest {

	@JsonProperty("documento")
	private String document;

    @JsonProperty("nome")
    private String name;

    @JsonProperty("idProposta")
    private String proposalId;

	public ClientFinancialAnalysisRequest(String document, String name, String proposalId) {
		this.document = document;
		this.name = name;
		this.proposalId = proposalId;
	}

	public String getDocument() {
		return document;
	}

	public String getName() {
		return name;
	}

	public String getProposalId() {
		return proposalId;
	}
	
    
}
