package br.com.zup.orange.proposal.card;

import java.math.BigDecimal;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.validation.annotation.Validated;

import br.com.zup.orange.validation.CpfOrCnpj;

@Validated
public class CardRequest {

	@NotBlank
	String biometryEncoded;

	@Deprecated
	public CardRequest() {};
	
	public CardRequest(@NotBlank String biometryEncoded) {
		this.biometryEncoded = biometryEncoded;
	}

	public String getBiometryEncoded() {
		return biometryEncoded;
	}
	
	
	

	
}
