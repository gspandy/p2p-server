package com.vcredit.jdev.p2p.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;

import com.vcredit.jdev.p2p.dto.CapitalCalculateQueryDto;


@SqlResultSetMappings({ @SqlResultSetMapping(name = "CapitalCalculateQueryDtoMapping", classes = { @ConstructorResult(targetClass = CapitalCalculateQueryDto.class, columns = {
	@ColumnResult(name = "operateFee",type=BigDecimal.class),@ColumnResult(name = "acReleaseAmt",type=BigDecimal.class),@ColumnResult(name = "tradeDesc",type=String.class),@ColumnResult(name = "invReleaseAmt",type=BigDecimal.class),@ColumnResult(name = "releaseTerms",type=Integer.class),
	@ColumnResult(name = "invRepayDate",type=Integer.class),@ColumnResult(name = "monthRepayAmt",type=BigDecimal.class) }) }) })
//投资项目与贷款人关系
@Entity
@Table(name="t_inv_act_ref")
public class InvestmentAccountReference {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_inv_act_ref_seq", unique = true, nullable = false)
	private Long investmentAccountReferenceSequence;// 流水号
	@Column(name = "t_inv_seq",nullable = false)	//投资项目流水号	
	private Long investmentSequence;
	@Column(name = "t_acct_seq",nullable = false)	//流水号	
	private Long accountSequence;
	@Column(name = "t_loan_data_seq",nullable = false)	//贷款人基础信息和贷款业务信息流水号	
	private Long loanDataSequence;
	public Long getInvestmentAccountReferenceSequence() {
		return investmentAccountReferenceSequence;
	}
	public void setInvestmentAccountReferenceSequence(
			Long investmentAccountReferenceSequence) {
		this.investmentAccountReferenceSequence = investmentAccountReferenceSequence;
	}
	public Long getInvestmentSequence() {
		return investmentSequence;
	}
	public void setInvestmentSequence(Long investmentSequence) {
		this.investmentSequence = investmentSequence;
	}
	public Long getAccountSequence() {
		return accountSequence;
	}
	public void setAccountSequence(Long accountSequence) {
		this.accountSequence = accountSequence;
	}
	public Long getLoanDataSequence() {
		return loanDataSequence;
	}
	public void setLoanDataSequence(Long loanDataSequence) {
		this.loanDataSequence = loanDataSequence;
	}

}
