package br.com.zup.orange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProposalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProposalApplication.class, args);
	}

}
