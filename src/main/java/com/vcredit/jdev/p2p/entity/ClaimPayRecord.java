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


//债权还款记录
@Entity
@Table(name="t_clm_precord")
public class ClaimPayRecord {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_clm_precord_seq", unique = true, nullable = false)
	private Long claimPayRecordSequence;// 流水号
	@Column(name = "t_inv_seq",nullable = false)
	private Long investmentSequence;// 投资项目流水号
	@Column(name = "clm_num",nullable = false)
	private Integer claimPayNumber;// 还款期数序号
	@Column(name = "clm_ppri",nullable = false,precision = 12, scale = 2)
	private BigDecimal claimPayPrincipal;// 实际还本金
	@Column(name = "clm_pint",nullable = false,precision = 12, scale = 2)
	private BigDecimal claimPayInterest;// 实际还利息
	@Column(name = "clm_pjint",nullable = false,precision = 12, scale = 2)
	private BigDecimal claimPayJusticeInterest;// 实际还罚息
	@Column(name = "clm_ptatol",nullable = false,precision = 12, scale = 2)
	private BigDecimal claimPayTatolSmount;// 实际还总额
	@Column(name = "clm_rpri",nullable = false,precision = 12, scale = 2)
	private BigDecimal claimPaySurplus;// 实际剩余本金
	@Column(name = "clm_periods",nullable = false,length=20)
	private String claimPayPeriod;// 还款期数
	@Column(name = "clm_pdate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date claimPayDate;// 还款时间
	@Column(name = "clm_ptype")
	private Integer claimPayType;// 还款性质 预留字段
	@Column(name = "clm_sdate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date claimPayStartDate;// 起息日
	@Column(name = "clm_pp_pmfee",nullable = false,precision = 12, scale = 2)
	private BigDecimal claimPayPlatformManagementFee;// 平台账号管理服务费 费率-参考数据字典
	@Column(name = "clm_pp_sus",nullable = false,precision = 12, scale = 2)
	private BigDecimal claimPaySubsidy;// 提前清贷补贴 费率-参考数据字典
	@Column(name = "t_pacct_seq",nullable = false)	//贷款用户P2P平台账号流水号	
	private Long payAccountSequence;
	@Column(name = "t_sacct_seq", nullable = false)	//vmoney-还款服务p2p平台账号	
	private Long payServiceAccountSequence;
	@Column(name = "t_racct_seq", nullable = false)	//vmoney-风险备用金p2p平台账号	
	private Long payRiskAccountSequence;
	@Column(name = "clm_pp_rfee", nullable = false,precision = 12, scale = 2) //风险备用金 费率-参考数据字典
	private BigDecimal claimPayPlanRiskFee;
	@Column(name = "cf_id")
	//汇付资金划拨流水号
	private BigDecimal cashFlowId;	
	public Long getClaimPayRecordSequence() {
		return claimPayRecordSequence;
	}
	public void setClaimPayRecordSequence(Long claimPayRecordSequence) {
		this.claimPayRecordSequence = claimPayRecordSequence;
	}
	public Long getInvestmentSequence() {
		return investmentSequence;
	}
	public void setInvestmentSequence(Long investmentSequence) {
		this.investmentSequence = investmentSequence;
	}
	public Integer getClaimPayNumber() {
		return claimPayNumber;
	}
	public void setClaimPayNumber(Integer claimPayNumber) {
		this.claimPayNumber = claimPayNumber;
	}
	public BigDecimal getClaimPayPrincipal() {
		return claimPayPrincipal;
	}
	public void setClaimPayPrincipal(BigDecimal claimPayPrincipal) {
		this.claimPayPrincipal = claimPayPrincipal;
	}
	public BigDecimal getClaimPayInterest() {
		return claimPayInterest;
	}
	public void setClaimPayInterest(BigDecimal claimPayInterest) {
		this.claimPayInterest = claimPayInterest;
	}
	public BigDecimal getClaimPayJusticeInterest() {
		return claimPayJusticeInterest;
	}
	public void setClaimPayJusticeInterest(BigDecimal claimPayJusticeInterest) {
		this.claimPayJusticeInterest = claimPayJusticeInterest;
	}
	public BigDecimal getClaimPayTatolSmount() {
		return claimPayTatolSmount;
	}
	public void setClaimPayTatolSmount(BigDecimal claimPayTatolSmount) {
		this.claimPayTatolSmount = claimPayTatolSmount;
	}
	public BigDecimal getClaimPaySurplus() {
		return claimPaySurplus;
	}
	public void setClaimPaySurplus(BigDecimal claimPaySurplus) {
		this.claimPaySurplus = claimPaySurplus;
	}
	public String getClaimPayPeriod() {
		return claimPayPeriod;
	}
	public void setClaimPayPeriod(String claimPayPeriod) {
		this.claimPayPeriod = claimPayPeriod;
	}
	public Date getClaimPayDate() {
		return claimPayDate;
	}
	public void setClaimPayDate(Date claimPayDate) {
		this.claimPayDate = claimPayDate;
	}
	public Integer getClaimPayType() {
		return claimPayType;
	}
	public void setClaimPayType(Integer claimPayType) {
		this.claimPayType = claimPayType;
	}
	public Date getClaimPayStartDate() {
		return claimPayStartDate;
	}
	public void setClaimPayStartDate(Date claimPayStartDate) {
		this.claimPayStartDate = claimPayStartDate;
	}
	public BigDecimal getClaimPayPlatformManagementFee() {
		return claimPayPlatformManagementFee;
	}
	public void setClaimPayPlatformManagementFee(
			BigDecimal claimPayPlatformManagementFee) {
		this.claimPayPlatformManagementFee = claimPayPlatformManagementFee;
	}
	public Long getPayAccountSequence() {
		return payAccountSequence;
	}
	public void setPayAccountSequence(Long payAccountSequence) {
		this.payAccountSequence = payAccountSequence;
	}
	public BigDecimal getClaimPaySubsidy() {
		return claimPaySubsidy;
	}
	public void setClaimPaySubsidy(BigDecimal claimPaySubsidy) {
		this.claimPaySubsidy = claimPaySubsidy;
	}
	public Long getPayServiceAccountSequence() {
		return payServiceAccountSequence;
	}
	public void setPayServiceAccountSequence(Long payServiceAccountSequence) {
		this.payServiceAccountSequence = payServiceAccountSequence;
	}
	public Long getPayRiskAccountSequence() {
		return payRiskAccountSequence;
	}
	public void setPayRiskAccountSequence(Long payRiskAccountSequence) {
		this.payRiskAccountSequence = payRiskAccountSequence;
	}
	public BigDecimal getClaimPayPlanRiskFee() {
		return claimPayPlanRiskFee;
	}
	public void setClaimPayPlanRiskFee(BigDecimal claimPayPlanRiskFee) {
		this.claimPayPlanRiskFee = claimPayPlanRiskFee;
	}
	public BigDecimal getCashFlowId() {
		return cashFlowId;
	}
	public void setCashFlowId(BigDecimal cashFlowId) {
		this.cashFlowId = cashFlowId;
	}

}
