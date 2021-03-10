package br.com.zup.orange.proposal;

import java.math.BigDecimal;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

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

	@Deprecated
	public Proposal() {}
	
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
	
	
	
}
