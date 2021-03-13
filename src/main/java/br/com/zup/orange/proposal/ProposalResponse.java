package br.com.zup.orange.proposal;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.validation.annotation.Validated;

import br.com.zup.orange.proposal.card.Card;
import br.com.zup.orange.validation.CpfOrCnpj;

public class ProposalResponse {

	private UUID id;

	@NotBlank
	private String name;

	private LocalDateTime createdAt;

	private ProposalStatus status;

	public ProposalResponse(UUID id, @NotBlank String name, LocalDateTime createdAt, ProposalStatus status) {
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
		this.status = status;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public ProposalStatus getStatus() {
		return status;
	}

	public void setStatus(ProposalStatus status) {
		this.status = status;
	}

	
}
