package com.vcredit.jdev.p2p.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vcredit.jdev.p2p.entity.LoanCut;

public interface LoanCutRepository extends JpaRepository<LoanCut, Long> {

	/**
	 * 查询小贷公司扣款信息
	 * 
	 * @return {@link LoanCut}的List
	 */
	public List<LoanCut> findByRecordStatus(Integer recordStatus);

	/**
	 * 查询小贷公司扣款信息
	 * 
	 * @return {@link LoanCut}的List
	 */
	public LoanCut findByInvestmentBusinessCodeAndLoanPeriod(String investmentBusinessCode, int loanPeriod);
}
