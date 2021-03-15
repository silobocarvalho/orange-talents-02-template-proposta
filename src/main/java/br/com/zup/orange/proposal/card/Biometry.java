package br.com.zup.orange.proposal.card;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Biometry {

	@Id
	@GeneratedValue
	private long id;
	
	@NotNull
	private LocalDateTime createdAt = LocalDateTime.now();

	@NotEmpty
	private byte[] biometry;

	public Biometry(@NotEmpty byte[] biometry) {
		this.biometry = biometry;
	}

	public long getId() {
		return id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public byte[] getBiometry() {
		return biometry;
	}
	
	
}
