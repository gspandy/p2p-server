package com.vcredit.jdev.p2p.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


//债权交易记录
@Entity
@Table(name="t_clm_trade")
public class AccountClaimTrade {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_clm_trade_seq", unique = true, nullable = false)
	private Long claimTradeSequence;// 流水号
	@Column(name = "t_act_clm_seq",nullable = false)
	private Long accountClaimSequence;// 用户债权流水号
	@Column(name = "clm_trade_idate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date clmTradeIssueDate;//发起时间
	@Column(name = "clm_trade_ddate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date claimTradeDealDate;// 成交时间
	@Column(name = "clm_trade_type",nullable = false)
	private Integer claimTradeType;// 交易性质 预留字段
	@Column(name = "clm_trade_stat",nullable = false)
	private Integer claimTradeStatus;// 交易状态 常量-参考数据字典
	@Column(name = "clm_tfee",nullable = false,precision = 12, scale = 2)
	private BigDecimal claimTradeP2pFee;// p2p平台手续费 费率-参考数据字典
	@Column(name = "t_pacct_seq",nullable = false)	//付费用户P2P平台账号流水号	
	private Long payAccountSequence;
	@Column(name = "t_gacct_seq",nullable = false)	//收费用户P2P平台账号流水号	
	private Long gatherAccountSequence;
	@Column(name = "t_gsacct_seq",nullable = false)	//vmoney-p2p平台收费账号流水号	
	private Long gatherServiceAccountSequence;
	@Column(name = "cf_id")
	//汇付资金划拨流水号
	private BigDecimal cashFlowId;
	
	public Long getClaimTradeSequence() {
		return claimTradeSequence;
	}
	public void setClaimTradeSequence(Long claimTradeSequence) {
		this.claimTradeSequence = claimTradeSequence;
	}
	public Long getAccountClaimSequence() {
		return accountClaimSequence;
	}
	public void setAccountClaimSequence(Long accountClaimSequence) {
		this.accountClaimSequence = accountClaimSequence;
	}
	public Integer getClaimTradeType() {
		return claimTradeType;
	}
	public void setClaimTradeType(Integer claimTradeType) {
		this.claimTradeType = claimTradeType;
	}
	public Integer getClaimTradeStatus() {
		return claimTradeStatus;
	}
	public void setClaimTradeStatus(Integer claimTradeStatus) {
		this.claimTradeStatus = claimTradeStatus;
	}
	public BigDecimal getClaimTradeP2pFee() {
		return claimTradeP2pFee;
	}
	public void setClaimTradeP2pFee(BigDecimal claimTradeP2pFee) {
		this.claimTradeP2pFee = claimTradeP2pFee;
	}
	public Date getClmTradeIssueDate() {
		return clmTradeIssueDate;
	}
	public void setClmTradeIssueDate(Date clmTradeIssueDate) {
		this.clmTradeIssueDate = clmTradeIssueDate;
	}
	public Date getClaimTradeDealDate() {
		return claimTradeDealDate;
	}
	public void setClaimTradeDealDate(Date claimTradeDealDate) {
		this.claimTradeDealDate = claimTradeDealDate;
	}
	public Long getPayAccountSequence() {
		return payAccountSequence;
	}
	public void setPayAccountSequence(Long payAccountSequence) {
		this.payAccountSequence = payAccountSequence;
	}
	public Long getGatherAccountSequence() {
		return gatherAccountSequence;
	}
	public void setGatherAccountSequence(Long gatherAccountSequence) {
		this.gatherAccountSequence = gatherAccountSequence;
	}
	public Long getGatherServiceAccountSequence() {
		return gatherServiceAccountSequence;
	}
	public void setGatherServiceAccountSequence(Long gatherServiceAccountSequence) {
		this.gatherServiceAccountSequence = gatherServiceAccountSequence;
	}
	public BigDecimal getCashFlowId() {
		return cashFlowId;
	}
	public void setCashFlowId(BigDecimal cashFlowId) {
		this.cashFlowId = cashFlowId;
	}

}
