package br.com.zup.orange.proposal.externalrequests.wallet;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.zup.orange.proposal.card.travel.TravelStatus;
import br.com.zup.orange.proposal.card.wallet.WalletAssociateStatus;

public class WalletAssociateExternalResponse {

	@JsonProperty("resultado")
	@NotBlank
	String walletAssociateStatus;
	
	@JsonProperty("id")
	@NotBlank
	String walletAssociateId;

	public WalletAssociateExternalResponse(@NotBlank String walletAssociateStatus, @NotBlank String walletAssociateId) {
		this.walletAssociateStatus = walletAssociateStatus;
		this.walletAssociateId = walletAssociateId;
	}

	public String getWalletAssociateStatus() {
		return walletAssociateStatus;
	}

	public String getWalletAssociateId() {
		return walletAssociateId;
	}

	public WalletAssociateStatus validateResult() {
		if(this.walletAssociateStatus.equals("ASSOCIADA")) {
			return WalletAssociateStatus.ASSOCIATED;
		}
		return WalletAssociateStatus.BLOCKED;
	}
	
	
}
