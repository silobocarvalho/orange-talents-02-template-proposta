package br.com.zup.orange.proposal.externalrequests.financialanalysis;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

// You can set url = serverUrl and PostMapping("/api/service) in each method.
@FeignClient(name = "analysis", url = "${financial.analysis.url}")
public interface ClientFinancialAnalysis {
	
	@PostMapping
	ClientFinancialAnalysisResponse requestFinancialAnalysis(ClientFinancialAnalysisRequest request);
}
