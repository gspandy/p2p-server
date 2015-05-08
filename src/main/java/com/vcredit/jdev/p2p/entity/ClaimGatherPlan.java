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



//债权收款计划
@Entity
@Table(name = "t_clm_gplan")
public class ClaimGatherPlan {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_clm_gplan_seq", unique = true, nullable = false)
	private Long claimGatherPlanSequence;// 流水号
	@Column(name = "t_act_inv_seq", nullable = false)
	private Long accountInvestmentSequence;// 用户获得的项目流水号
	@Column(name = "clm_gp_num", nullable = false)
	private Integer claimGatherPlanNumber;// 收款期数序号
	@Column(name = "clm_gpapri", nullable = false, precision = 20, scale = 10)
	private BigDecimal claimGatherPlanActualPrincipal;// 计划收本金
	@Column(name = "clm_gpppri", nullable = false, precision = 12, scale = 2)
	private BigDecimal claimGatherPlanPretendPrincipal;// 计划收本金（四舍五入）
	@Column(name = "clm_gpaint", nullable = false, precision = 20, scale = 10)
	private BigDecimal claimGatherPlanActualInterest;// 计划收利息 费率-参考数据字典
	@Column(name = "clm_gppint", nullable = false, precision = 12, scale = 2)
	private BigDecimal claimGatherPlanPretendInterest;// 计划收利息（四舍五入）
	@Column(name = "clm_gpajint", nullable = false, precision = 20, scale = 10)
	private BigDecimal claimGatherPlanActualJusticeInterest;// 计划收罚息 费率-参考数据字典
	@Column(name = "clm_gppjint", nullable = false, precision = 12, scale = 2)
	private BigDecimal claimGatherPlanPretendJusticeInterest;// 计划收罚息（四舍五入）
	@Column(name = "clm_gpatotal", nullable = false, precision = 20, scale = 10)
	private BigDecimal claimGatherPlanActualTotalAmount;// 计划收总额
	@Column(name = "clm_gpptotal", nullable = false, precision = 12, scale = 2)
	private BigDecimal claimGatherPlanPretendTotalAmount;// 计划收总额（四舍五入）
	
	@Column(name = "clm_gpas", nullable = false, precision = 20, scale = 10)
	private BigDecimal claimGatherPlanActualSurplus;// 计划剩余本金
	
	@Column(name = "clm_gpps", nullable = false, precision = 12, scale = 2)
	private BigDecimal claimGatherPlanPretendSurplus;// 计划剩余本金（四舍五入）
	
	@Column(name = "clm_gp_periods", nullable = false,length=20)
	private String claim_gather_plan_period;// 收款期数
	
	@Column(name = "clm_gp_cdate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date claimGatherPlanCreateDate;// 创建时间
	
	@Column(name = "clm_gp_ndate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date claimGatherPlanNatureDate;// 账单日
	
	@Column(name = "clm_gp_stat", nullable = false)
	private Integer claimGatherPlanStatus;// 收款计划状态 常量-参考数据字典
	
	@Column(name = "t_pacct_seq", nullable = false)	//付费用户P2P平台账号流水号	
	private Long payAccountSequence;
	@Column(name = "t_gacct_seq", nullable = false)	//收费用户P2P平台账号流水号	
	
	private Long gatherAccountSequence;
	@Column(name = "t_jacct_seq", nullable = false)	//支付罚息人P2P平台账号流水号	
	private Long justiceFeeAccountSequence;
	@Column(name = "act_clm_amfee", nullable = false, precision = 20, scale = 10)	//p2p平台投资管理费（四舍五入）	
	private BigDecimal accountClaimActualManageFee;
	@Column(name = "act_clm_pmfee", nullable = false, precision = 12, scale = 2)	//p2p平台投资管理费	
	private BigDecimal accountClaimShouldManageFee;
	@Column(name = "t_gsacct_seq", nullable = false)	//vmoney-p2p平台收费账号流水号	
	private Integer gatherServiceAccountSequence;
	@Column(name = "t_act_order_seq")
	private Long accountOrderSequence;// 流水号
	@Column(name = "rec_edate")	//记录更新时间	
	@Temporal(TemporalType.TIMESTAMP)
	private Date recordEditDate;	
	
	public Long getClaimGatherPlanSequence() {
		return claimGatherPlanSequence;
	}
	public void setClaimGatherPlanSequence(Long claimGatherPlanSequence) {
		this.claimGatherPlanSequence = claimGatherPlanSequence;
	}
	public Integer getClaimGatherPlanNumber() {
		return claimGatherPlanNumber;
	}
	public void setClaimGatherPlanNumber(Integer claimGatherPlanNumber) {
		this.claimGatherPlanNumber = claimGatherPlanNumber;
	}
	public BigDecimal getClaimGatherPlanActualPrincipal() {
		return claimGatherPlanActualPrincipal;
	}
	public void setClaimGatherPlanActualPrincipal(
			BigDecimal claimGatherPlanActualPrincipal) {
		this.claimGatherPlanActualPrincipal = claimGatherPlanActualPrincipal;
	}
	public BigDecimal getClaimGatherPlanPretendPrincipal() {
		return claimGatherPlanPretendPrincipal;
	}
	public void setClaimGatherPlanPretendPrincipal(
			BigDecimal claimGatherPlanPretendPrincipal) {
		this.claimGatherPlanPretendPrincipal = claimGatherPlanPretendPrincipal;
	}
	public BigDecimal getClaimGatherPlanActualInterest() {
		return claimGatherPlanActualInterest;
	}
	public void setClaimGatherPlanActualInterest(
			BigDecimal claimGatherPlanActualInterest) {
		this.claimGatherPlanActualInterest = claimGatherPlanActualInterest;
	}
	public BigDecimal getClaimGatherPlanPretendInterest() {
		return claimGatherPlanPretendInterest;
	}
	public void setClaimGatherPlanPretendInterest(
			BigDecimal claimGatherPlanPretendInterest) {
		this.claimGatherPlanPretendInterest = claimGatherPlanPretendInterest;
	}
	public BigDecimal getClaimGatherPlanActualJusticeInterest() {
		return claimGatherPlanActualJusticeInterest;
	}
	public void setClaimGatherPlanActualJusticeInterest(
			BigDecimal claimGatherPlanActualJusticeInterest) {
		this.claimGatherPlanActualJusticeInterest = claimGatherPlanActualJusticeInterest;
	}
	public BigDecimal getClaimGatherPlanPretendJusticeInterest() {
		return claimGatherPlanPretendJusticeInterest;
	}
	public void setClaimGatherPlanPretendJusticeInterest(
			BigDecimal claimGatherPlanPretendJusticeInterest) {
		this.claimGatherPlanPretendJusticeInterest = claimGatherPlanPretendJusticeInterest;
	}
	public BigDecimal getClaimGatherPlanActualTotalAmount() {
		return claimGatherPlanActualTotalAmount;
	}
	public void setClaimGatherPlanActualTotalAmount(
			BigDecimal claimGatherPlanActualTotalAmount) {
		this.claimGatherPlanActualTotalAmount = claimGatherPlanActualTotalAmount;
	}
	public BigDecimal getClaimGatherPlanPretendTotalAmount() {
		return claimGatherPlanPretendTotalAmount;
	}
	public void setClaimGatherPlanPretendTotalAmount(
			BigDecimal claimGatherPlanPretendTotalAmount) {
		this.claimGatherPlanPretendTotalAmount = claimGatherPlanPretendTotalAmount;
	}
	public BigDecimal getClaimGatherPlanActualSurplus() {
		return claimGatherPlanActualSurplus;
	}
	public void setClaimGatherPlanActualSurplus(
			BigDecimal claimGatherPlanActualSurplus) {
		this.claimGatherPlanActualSurplus = claimGatherPlanActualSurplus;
	}
	public BigDecimal getClaimGatherPlanPretendSurplus() {
		return claimGatherPlanPretendSurplus;
	}
	public void setClaimGatherPlanPretendSurplus(
			BigDecimal claimGatherPlanPretendSurplus) {
		this.claimGatherPlanPretendSurplus = claimGatherPlanPretendSurplus;
	}
	public String getClaim_gather_plan_period() {
		return claim_gather_plan_period;
	}
	public void setClaim_gather_plan_period(String claim_gather_plan_period) {
		this.claim_gather_plan_period = claim_gather_plan_period;
	}
	public Date getClaimGatherPlanCreateDate() {
		return claimGatherPlanCreateDate;
	}
	public void setClaimGatherPlanCreateDate(Date claimGatherPlanCreateDate) {
		this.claimGatherPlanCreateDate = claimGatherPlanCreateDate;
	}
	public Date getClaimGatherPlanNatureDate() {
		return claimGatherPlanNatureDate;
	}
	public void setClaimGatherPlanNatureDate(Date claimGatherPlanNatureDate) {
		this.claimGatherPlanNatureDate = claimGatherPlanNatureDate;
	}
	public Integer getClaimGatherPlanStatus() {
		return claimGatherPlanStatus;
	}
	public void setClaimGatherPlanStatus(Integer claimGatherPlanStatus) {
		this.claimGatherPlanStatus = claimGatherPlanStatus;
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
	public Long getJusticeFeeAccountSequence() {
		return justiceFeeAccountSequence;
	}
	public void setJusticeFeeAccountSequence(Long justiceFeeAccountSequence) {
		this.justiceFeeAccountSequence = justiceFeeAccountSequence;
	}
	public Long getAccountInvestmentSequence() {
		return accountInvestmentSequence;
	}
	public void setAccountInvestmentSequence(Long accountInvestmentSequence) {
		this.accountInvestmentSequence = accountInvestmentSequence;
	}
	public BigDecimal getAccountClaimActualManageFee() {
		return accountClaimActualManageFee;
	}
	public void setAccountClaimActualManageFee(
			BigDecimal accountClaimActualManageFee) {
		this.accountClaimActualManageFee = accountClaimActualManageFee;
	}
	public BigDecimal getAccountClaimShouldManageFee() {
		return accountClaimShouldManageFee;
	}
	public void setAccountClaimShouldManageFee(
			BigDecimal accountClaimShouldManageFee) {
		this.accountClaimShouldManageFee = accountClaimShouldManageFee;
	}
	public Integer getGatherServiceAccountSequence() {
		return gatherServiceAccountSequence;
	}
	public void setGatherServiceAccountSequence(Integer gatherServiceAccountSequence) {
		this.gatherServiceAccountSequence = gatherServiceAccountSequence;
	}
	public Long getAccountOrderSequence() {
		return accountOrderSequence;
	}
	public void setAccountOrderSequence(Long accountOrderSequence) {
		this.accountOrderSequence = accountOrderSequence;
	}
	public Date getRecordEditDate() {
		return recordEditDate;
	}
	public void setRecordEditDate(Date recordEditDate) {
		this.recordEditDate = recordEditDate;
	}

}
