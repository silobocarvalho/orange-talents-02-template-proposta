package br.com.zup.orange.proposal;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.transaction.Transactional;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.web.client.HttpStatusCodeException;

import br.com.zup.orange.proposal.card.Card;
import br.com.zup.orange.proposal.card.CardBiometryRequest;
import br.com.zup.orange.proposal.externalrequests.card.CardAPIClient;
import br.com.zup.orange.proposal.externalrequests.card.CardRequest;
import br.com.zup.orange.proposal.externalrequests.card.CardResponse;
import br.com.zup.orange.proposal.externalrequests.financialanalysis.ClientFinancialAnalysis;
import br.com.zup.orange.proposal.externalrequests.financialanalysis.ClientFinancialAnalysisRequest;
import br.com.zup.orange.proposal.externalrequests.financialanalysis.ClientFinancialAnalysisResponse;
import br.com.zup.orange.proposal.externalrequests.financialanalysis.SolicitationStatus;
import br.com.zup.orange.security.StringAttributeConverter;
import br.com.zup.orange.validation.CpfOrCnpj;
import feign.FeignException;

@Entity
public class Proposal {

	@Id
	@GeneratedValue
	private UUID id;

	@CpfOrCnpj
	@NotBlank
	@Convert(converter = StringAttributeConverter.class)
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

	private LocalDateTime createdAt = LocalDateTime.now();

	@Enumerated(EnumType.STRING)
	private ProposalStatus status;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "card_id")
	private Card card;

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

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public Card getCard() {
		return card;
	}

	@Override
	public String toString() {
		return "Proposal [id=" + id + ", document=" + document + ", email=" + email + ", name=" + name + ", address="
				+ address + ", salary=" + salary + ", createdAt=" + createdAt + ", status=" + status + ", card=" + card
				+ "]";
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

			if (responseFinancialAnalysis.getStatus().equals(SolicitationStatus.SEM_RESTRICAO)) {
				this.status = ProposalStatus.ELEGIVEL;
			}

		} catch (FeignException e) {
			this.status = ProposalStatus.NAO_ELEGIVEL;
		}
	}

	@Transactional
	public void updateCardInfo(CardAPIClient cardAssociate, ProposalRepository proposalRepository) {

		CardResponse cardResponse = cardAssociate.getCardInfo(new CardRequest(this.id.toString()));

		try {
			if (cardResponse.getCardNumber() != null) {
				this.card = cardResponse.toModel(proposalRepository, this.id);
				proposalRepository.save(this);
			}
		} catch (FeignException fe) {
			fe.printStackTrace();
		}
	}

	public ProposalResponse toResponse() {
		return new ProposalResponse(this.id, this.name, this.createdAt, this.status);
	}

}
