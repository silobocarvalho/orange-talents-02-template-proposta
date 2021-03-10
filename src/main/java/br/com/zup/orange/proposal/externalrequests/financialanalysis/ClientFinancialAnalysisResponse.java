package br.com.zup.orange.proposal.externalrequests.financialanalysis;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ClientFinancialAnalysisResponse {

	@JsonProperty("documento")
	private String document;

	@JsonProperty("nome")
	private String name;

	@JsonProperty("resultadoSolicitacao")
	private SolicitationStatus status;

	@JsonProperty("idProposta")
	private String proposalId;

	public ClientFinancialAnalysisResponse(String document, String name, SolicitationStatus status,
			String proposalId) {
		this.document = document;
		this.name = name;
		this.status = status;
		this.proposalId = proposalId;
	}

	public String getDocument() {
		return document;
	}

	public String getName() {
		return name;
	}

	public SolicitationStatus getStatus() {
		return status;
	}

	public String getProposalId() {
		return proposalId;
	}

}
