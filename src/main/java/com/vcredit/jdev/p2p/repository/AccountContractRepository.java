package com.vcredit.jdev.p2p.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vcredit.jdev.p2p.entity.AccountContract;

public interface AccountContractRepository extends JpaRepository<AccountContract, Long> {
	
	public List<AccountContract> findByContractNumber(String contractNumber);
	
	@Query(nativeQuery = true, value = "select ac.* from  t_act_cont ac  where  ac.t_inv_seq = ?1 ")
	public List<AccountContract> findAccountContractInvestment(Long investmentSequence);
}
