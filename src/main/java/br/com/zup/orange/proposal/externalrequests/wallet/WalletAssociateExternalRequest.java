package br.com.zup.orange.proposal.externalrequests.wallet;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WalletAssociateExternalRequest {

	@JsonProperty("email")
	@Email
	@NotNull
	private String email;

	@JsonProperty("carteira")
	@NotBlank
	private String walletName;

	public WalletAssociateExternalRequest(@Email @NotNull String email, @NotBlank String walletName) {
		this.email = email;
		this.walletName = walletName;
	}

	public String getEmail() {
		return email;
	}

	public String getWalletName() {
		return walletName;
	}

}
