package br.com.zup.orange.proposal.card.wallet;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.zup.orange.proposal.externalrequests.card.travelnotice.TravelNoticeExternalRequest;
import br.com.zup.orange.proposal.externalrequests.wallet.WalletAssociateExternalRequest;

@Entity
public class WalletAssociate {

	@Id
	@GeneratedValue
	private UUID id;

	@NotBlank
	private String walletName;

	@Email
	String email;

	@NotNull
	LocalDateTime createdAt = LocalDateTime.now();

	WalletAssociateStatus walletAssociateStatus;

	@Deprecated
	public WalletAssociate() {};
	
	public WalletAssociate(@NotBlank String walletName, @Email String email) {
		this.walletName = walletName;
		this.email = email;
	}

	public UUID getId() {
		return id;
	}

	public String getWalletName() {
		return walletName;
	}

	public String getEmail() {
		return email;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}


	public WalletAssociateStatus getWalletAssociateStatus() {
		return walletAssociateStatus;
	}

	public void associateWallet(WalletAssociateStatus walletStatus) {
		this.walletAssociateStatus = walletStatus;
	}
	
	public WalletAssociateExternalRequest toRequest() {
		return new WalletAssociateExternalRequest(this.email, this.walletName);
	}

}
