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

//债权还款计划状态历史
@Entity
@Table(name="t_clm_pp_his")
public class ClaimPayPlanHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_clm_pp_his_seq", unique = true, nullable = false)
	private Long claimPayPlanHistorySequence;// 流水号
	@Column(name = "t_clm_pplan_seq", nullable = false)
	private Long claimPayPlanSequence;// 流水号
	@Column(name = "t_inv_seq",nullable = false)
	private Long investmentSequence;// 投资项目流水号
	@Column(name = "clm_pp_num",nullable = false)
	private Integer claimPayPlanNumber;// 还款期数序号
	@Column(name = "clm_pp_pri",nullable = false,precision = 12, scale = 2)
	private BigDecimal claimPayPlanPrincipal;// 应还本金
	@Column(name = "clm_pp_int",nullable = false,precision = 12, scale = 2)
	private BigDecimal claimPayPlanInterest;// 应还利息
	@Column(name = "clm_pp_jint",nullable = false,precision = 12, scale = 2)
	private BigDecimal claimPayPlanJusticeInterest;// 应还罚息
	@Column(name = "clm_pp_tatol",nullable = false,precision = 12, scale = 2)
	private BigDecimal claimPayPlanTotalAmount;// 应还总额
	@Column(name = "clm_pp_surplus",nullable = false,precision = 12, scale = 2)
	private BigDecimal claimPayPlanSurplus;// 计划剩余本金
	@Column(name = "clm_pp_periods",nullable = false,length=20)
	private String claimPayPlanPeriod;// 还款期数
	@Column(name = "clm_pp_cdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date claimPayPlanCreateDate;// 创建时间
	@Column(name = "clm_pp_ndate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date claimPayPlanNatureDate;// 账单日
	@Column(name = "clm_pp_stat",nullable = false)
	private Integer claimPayPlanStatus;// 还款计划状态 常量-参考数据字典
	@Column(name = "clm_pp_pmfee",nullable = false,precision = 12, scale = 2)
	private BigDecimal claimPayPlanPlatformManagementFee;// 平台账号管理服务费 费率-参考数据字典
	@Column(name = "clm_pp_ddays",nullable = false)
	private Integer claimPayPlanDelayDays;// 逾期天数
	@Column(name = "clm_pp_udays",nullable = false)
	private Integer claimPayPlanUnkownDays;// 未决天数
	@Column(name = "clm_pp_sus",nullable = false,precision = 12, scale = 2)
	private BigDecimal claimPayPlanSubsidy;// 提前清贷补贴 费率-参考数据字典
	@Column(name = "t_pacct_seq", nullable = false)	//贷款用户P2P平台账号流水号	
	private Long payAccountSequence;
	@Column(name = "clm_pp_rfee", nullable = false,precision = 12, scale = 2) //风险备用金 
	private BigDecimal claimPayPlanRiskFee;
	@Column(name = "t_sacct_seq", nullable = false)	//vmoney-还款服务p2p平台账号	
	private Long payServiceAccountSequence;
	@Column(name = "t_racct_seq", nullable = false)	//vmoney-风险备用金p2p平台账号	
	private Long payRiskAccountSequence;
	@Column(name = "rec_cdate",nullable = false)	//入库时间	
	@Temporal(TemporalType.TIMESTAMP)
	private Date recordCreateDate;
	@Column(name = "rec_edate")	//记录更新时间	
	@Temporal(TemporalType.TIMESTAMP)
	private Date recordEditDate;
	public Long getClaimPayPlanHistorySequence() {
		return claimPayPlanHistorySequence;
	}
	public void setClaimPayPlanHistorySequence(Long claimPayPlanHistorySequence) {
		this.claimPayPlanHistorySequence = claimPayPlanHistorySequence;
	}
	public Long getClaimPayPlanSequence() {
		return claimPayPlanSequence;
	}
	public void setClaimPayPlanSequence(Long claimPayPlanSequence) {
		this.claimPayPlanSequence = claimPayPlanSequence;
	}
	public Long getInvestmentSequence() {
		return investmentSequence;
	}
	public void setInvestmentSequence(Long investmentSequence) {
		this.investmentSequence = investmentSequence;
	}
	public Integer getClaimPayPlanNumber() {
		return claimPayPlanNumber;
	}
	public void setClaimPayPlanNumber(Integer claimPayPlanNumber) {
		this.claimPayPlanNumber = claimPayPlanNumber;
	}
	public BigDecimal getClaimPayPlanPrincipal() {
		return claimPayPlanPrincipal;
	}
	public void setClaimPayPlanPrincipal(BigDecimal claimPayPlanPrincipal) {
		this.claimPayPlanPrincipal = claimPayPlanPrincipal;
	}
	public BigDecimal getClaimPayPlanInterest() {
		return claimPayPlanInterest;
	}
	public void setClaimPayPlanInterest(BigDecimal claimPayPlanInterest) {
		this.claimPayPlanInterest = claimPayPlanInterest;
	}
	public BigDecimal getClaimPayPlanJusticeInterest() {
		return claimPayPlanJusticeInterest;
	}
	public void setClaimPayPlanJusticeInterest(
			BigDecimal claimPayPlanJusticeInterest) {
		this.claimPayPlanJusticeInterest = claimPayPlanJusticeInterest;
	}
	public BigDecimal getClaimPayPlanTotalAmount() {
		return claimPayPlanTotalAmount;
	}
	public void setClaimPayPlanTotalAmount(BigDecimal claimPayPlanTotalAmount) {
		this.claimPayPlanTotalAmount = claimPayPlanTotalAmount;
	}
	public BigDecimal getClaimPayPlanSurplus() {
		return claimPayPlanSurplus;
	}
	public void setClaimPayPlanSurplus(BigDecimal claimPayPlanSurplus) {
		this.claimPayPlanSurplus = claimPayPlanSurplus;
	}
	public String getClaimPayPlanPeriod() {
		return claimPayPlanPeriod;
	}
	public void setClaimPayPlanPeriod(String claimPayPlanPeriod) {
		this.claimPayPlanPeriod = claimPayPlanPeriod;
	}
	public Date getClaimPayPlanCreateDate() {
		return claimPayPlanCreateDate;
	}
	public void setClaimPayPlanCreateDate(Date claimPayPlanCreateDate) {
		this.claimPayPlanCreateDate = claimPayPlanCreateDate;
	}
	public Date getClaimPayPlanNatureDate() {
		return claimPayPlanNatureDate;
	}
	public void setClaimPayPlanNatureDate(Date claimPayPlanNatureDate) {
		this.claimPayPlanNatureDate = claimPayPlanNatureDate;
	}
	public Integer getClaimPayPlanStatus() {
		return claimPayPlanStatus;
	}
	public void setClaimPayPlanStatus(Integer claimPayPlanStatus) {
		this.claimPayPlanStatus = claimPayPlanStatus;
	}
	public BigDecimal getClaimPayPlanPlatformManagementFee() {
		return claimPayPlanPlatformManagementFee;
	}
	public void setClaimPayPlanPlatformManagementFee(
			BigDecimal claimPayPlanPlatformManagementFee) {
		this.claimPayPlanPlatformManagementFee = claimPayPlanPlatformManagementFee;
	}
	public Integer getClaimPayPlanDelayDays() {
		return claimPayPlanDelayDays;
	}
	public void setClaimPayPlanDelayDays(Integer claimPayPlanDelayDays) {
		this.claimPayPlanDelayDays = claimPayPlanDelayDays;
	}
	public Integer getClaimPayPlanUnkownDays() {
		return claimPayPlanUnkownDays;
	}
	public void setClaimPayPlanUnkownDays(Integer claimPayPlanUnkownDays) {
		this.claimPayPlanUnkownDays = claimPayPlanUnkownDays;
	}
	public BigDecimal getClaimPayPlanSubsidy() {
		return claimPayPlanSubsidy;
	}
	public void setClaimPayPlanSubsidy(BigDecimal claimPayPlanSubsidy) {
		this.claimPayPlanSubsidy = claimPayPlanSubsidy;
	}
	public Long getPayAccountSequence() {
		return payAccountSequence;
	}
	public void setPayAccountSequence(Long payAccountSequence) {
		this.payAccountSequence = payAccountSequence;
	}
	public BigDecimal getClaimPayPlanRiskFee() {
		return claimPayPlanRiskFee;
	}
	public void setClaimPayPlanRiskFee(BigDecimal claimPayPlanRiskFee) {
		this.claimPayPlanRiskFee = claimPayPlanRiskFee;
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
	public Date getRecordCreateDate() {
		return recordCreateDate;
	}
	public void setRecordCreateDate(Date recordCreateDate) {
		this.recordCreateDate = recordCreateDate;
	}
	public Date getRecordEditDate() {
		return recordEditDate;
	}
	public void setRecordEditDate(Date recordEditDate) {
		this.recordEditDate = recordEditDate;
	}	

}
