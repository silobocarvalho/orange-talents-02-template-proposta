package br.com.zup.orange.proposal.externalrequests.card;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "card.associate", url = "${card.associate.url}")
public interface CardAssociate {
	
	@GetMapping
	CardResponse getCardInfo(CardRequest cardRequest);
}
