package br.com.zup.orange.proposal.externalrequests;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "healthcheck", url = "${health.check.url}")
public interface HealthRequester {

	@GetMapping
	HealthCheckResponse healthCheck();
	
}
