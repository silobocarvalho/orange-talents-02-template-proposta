package br.com.zup.orange.proposal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProposalRepository extends JpaRepository<Proposal, Long>{

	public boolean existsProposalByDocument(String document);
	public Proposal findByDocument(String document);
	
	
}
