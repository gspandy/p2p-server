package com.vcredit.jdev.p2p.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vcredit.jdev.p2p.entity.Account;
import com.vcredit.jdev.p2p.entity.LoanData;

public interface BizDataRepository extends JpaRepository<LoanData, Long> {
	public LoanData findByInvestmentBusinessCode(String investmentBusinessCode);
	
	public LoanData findByInvestmentBusinessCodeAndInvestmentSource(String investmentBusinessCode,Integer investmentSource);
	
	
}
