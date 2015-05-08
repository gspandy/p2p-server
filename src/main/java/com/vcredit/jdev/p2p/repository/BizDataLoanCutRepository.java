package com.vcredit.jdev.p2p.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vcredit.jdev.p2p.entity.LoanCut;
import com.vcredit.jdev.p2p.entity.LoanData;

public interface BizDataLoanCutRepository extends JpaRepository<LoanCut, Long> {
	public LoanCut findByInvestmentBusinessCodeAndLoanPeriod(String investmentBusinessCode, Integer loanPeriod);
}
