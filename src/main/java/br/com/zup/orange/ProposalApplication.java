package br.com.zup.orange;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableAsync
@EnableScheduling
public class ProposalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProposalApplication.class, args);
	}

}
