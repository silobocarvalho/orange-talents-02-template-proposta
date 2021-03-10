package br.com.zup.orange.proposal.externalrequests.financialanalysis;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "analysis", url = "http://localhost:9999/api/solicitacao")
public interface ClientFinancialAnalysis {
	
	@PostMapping
	ClientFinancialAnalysisResponse requestFinancialAnalysis(ClientFinancialAnalysisRequest request);
}
