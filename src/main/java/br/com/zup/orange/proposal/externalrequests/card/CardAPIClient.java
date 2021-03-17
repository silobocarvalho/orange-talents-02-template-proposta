package br.com.zup.orange.proposal.externalrequests.card;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import br.com.zup.orange.proposal.card.CardBiometryRequest;
import br.com.zup.orange.proposal.externalrequests.card.travelnotice.TravelNoticeExternalRequest;
import br.com.zup.orange.proposal.externalrequests.card.travelnotice.TravelNoticeExternalResponse;

@FeignClient(name = "card.api", url = "${card.api.url}")
public interface CardAPIClient {
	
	@GetMapping
	CardResponse getCardInfo(CardRequest cardRequest);
	
	@PostMapping("/{id}/bloqueios")
	CardBlockResponse blockCard(@PathVariable String id, CardBlockRequest cardBlockRequest);
	
	@PostMapping("/{id}/avisos")
	TravelNoticeExternalResponse travelNotice(@PathVariable String id, TravelNoticeExternalRequest travelNoticeExternalRequest);
}
