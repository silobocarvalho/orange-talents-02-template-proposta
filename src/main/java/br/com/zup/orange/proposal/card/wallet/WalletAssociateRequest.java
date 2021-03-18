package br.com.zup.orange.proposal.card.wallet;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

@Validated
public class WalletAssociateRequest {


	@NotBlank
	String walletName;
	
	@Email
	@NotNull
	String email;

	public WalletAssociateRequest(@NotBlank String walletName, @Email String email) {
		this.walletName = walletName;
		this.email = email;
	}

	public String getWalletName() {
		return walletName;
	}

	public String getEmail() {
		return email;
	}

	public WalletAssociate toModel() {
		return new WalletAssociate(this.walletName, this.email);
	}
	
	
}
