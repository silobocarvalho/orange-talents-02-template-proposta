package br.com.zup.orange.proposal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal, Long>{

	public boolean existsProposalByDocument(String document);
	
	public Proposal findByDocument(String document);
	//public List<Proposal> findByStatusOrderByCreatedAtAsc(ProposalStatus status);
	public List<Proposal> findByStatusAndCardIsNullOrderByCreatedAtAsc(ProposalStatus status);
	
	public Optional<Proposal> findById(UUID id);
	
}
