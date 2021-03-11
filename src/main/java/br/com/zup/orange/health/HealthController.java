package br.com.zup.orange.health;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.zup.orange.proposal.externalrequests.HealthCheckResponse;
import br.com.zup.orange.proposal.externalrequests.HealthRequester;

@RestController
@Validated
@RequestMapping("/health")
public class HealthController {

	@Autowired
	HealthRequester health;

	@GetMapping
	public ResponseEntity<Object> check() {

		HealthCheckResponse response = health.healthCheck();

		if (response.getStatus().equals(HealthCheckStatus.UP)) {
			return ResponseEntity.status(HttpStatus.OK).build();
		}

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	}
}
