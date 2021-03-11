package br.com.zup.orange.proposal.externalrequests;

import br.com.zup.orange.health.HealthCheckStatus;

public class HealthCheckResponse {

	private HealthCheckStatus status;

	@Deprecated
	public HealthCheckResponse() {}
	
	public HealthCheckResponse(HealthCheckStatus status) {
		this.status = status;
	}

	public HealthCheckStatus getStatus() {
		return status;
	}

}
