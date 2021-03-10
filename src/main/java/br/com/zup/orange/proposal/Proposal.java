package br.com.zup.orange.proposal;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpStatusCodeException;

import br.com.zup.orange.proposal.externalrequests.financialanalysis.ClientFinancialAnalysis;
import br.com.zup.orange.proposal.externalrequests.financialanalysis.ClientFinancialAnalysisRequest;
import br.com.zup.orange.proposal.externalrequests.financialanalysis.ClientFinancialAnalysisResponse;
import br.com.zup.orange.proposal.externalrequests.financialanalysis.SolicitationStatus;
import br.com.zup.orange.validation.CpfOrCnpj;

@Entity
public class Proposal {

	@Id
	@GeneratedValue
	private UUID id;

	@CpfOrCnpj
	@NotBlank
	private String document;

	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String name;

	@NotBlank
	private String address;

	@NotNull
	@PositiveOrZero
	private BigDecimal salary;

	@Enumerated
	private ProposalStatus status;

	@Deprecated
	public Proposal() {
	}

	public Proposal(@NotBlank String document, @NotBlank @Email String email, @NotBlank String name,
			@NotBlank String address, @NotNull @PositiveOrZero BigDecimal salary) {
		this.document = document;
		this.email = email;
		this.name = name;
		this.address = address;
		this.salary = salary;
	}

	public UUID getId() {
		return id;
	}

	public String getDocument() {
		return document;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}

	public BigDecimal getSalary() {
		return salary;
	}

	public ProposalStatus getStatus() {
		return status;
	}

	public ClientFinancialAnalysisRequest toFinancialAnalysis() {
		if (this.id != null) {
			return new ClientFinancialAnalysisRequest(this.document, this.name, this.id.toString());
		} else {
			return null;
		}

	}

	public void updateFinancialAnalysis(ClientFinancialAnalysis clientFinancialAnalysis) {
		// Verify if user has restrictions in his name
		// http://localhost:9999/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config#/
		ClientFinancialAnalysisRequest clientFinancialAnalysisRequest = this.toFinancialAnalysis();

		try {
			ClientFinancialAnalysisResponse responseFinancialAnalysis = clientFinancialAnalysis
					.requestFinancialAnalysis(clientFinancialAnalysisRequest);

			if (responseFinancialAnalysis.getStatus() == SolicitationStatus.SEM_RESTRICAO) {
				this.status = ProposalStatus.ELEGIVEL;
			}

		} catch (Exception e) {
			this.status = ProposalStatus.NAO_ELEGIVEL;
		}

		
	}

}
