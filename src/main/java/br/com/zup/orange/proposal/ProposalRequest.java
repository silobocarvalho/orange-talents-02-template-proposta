package br.com.zup.orange.proposal;

import java.math.BigDecimal;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.validation.annotation.Validated;

import br.com.zup.orange.validation.CpfOrCnpj;

@Validated
public class ProposalRequest {

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

	public ProposalRequest(@NotBlank String document, @NotBlank @Email String email, @NotBlank String name,
			@NotBlank String address, @NotNull @PositiveOrZero BigDecimal salary) {
		this.document = document;
		this.email = email;
		this.name = name;
		this.address = address;
		this.salary = salary;
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



	@Override
	public String toString() {
		return "ProposalRequest [document=" + document + ", email=" + email + ", name=" + name + ", address=" + address
				+ ", salary=" + salary + "]";
	}

	public Proposal toModel() {

		return new Proposal(this.document, this.email, this.name, this.address, this.salary);
	}
	
}
