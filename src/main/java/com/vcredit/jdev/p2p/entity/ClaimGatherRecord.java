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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.vcredit.jdev.p2p.dto.ClaimGatherAmtDto;

//债权收款记录
@SqlResultSetMappings({ @SqlResultSetMapping(name = "ClaimGatherRecordMapping", classes = { @ConstructorResult(targetClass = ClaimGatherAmtDto.class, columns = {
		@ColumnResult(name = "clmGnum", type = Integer.class), @ColumnResult(name = "clmGptotal", type = Double.class), }) }) })
@Entity
@Table(name = "t_clm_grecord")
public class ClaimGatherRecord {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_clm_grecord_seq", unique = true, nullable = false)
	private Long claimGatherRecordSequence;// 流水号
	@Column(name = "t_act_clm_seq", nullable = false)
	private Long accountClaimSequence;// 投资人债权流水号
	@Column(name = "clm_gnum", nullable = false)
	private Integer claimGatherNumber;// 收款期数序号
	@Column(name = "clm_gapri", nullable = false, precision = 20, scale = 10)
	private BigDecimal claimGatherActualPrincipal; //计划收本金
	@Column(name = "clm_gppri", nullable = false, precision = 12, scale = 2)
	private BigDecimal claimGatherPretendPrincipal;//计划收本金（四舍五入）
	@Column(name = "clm_gaint", nullable = false, precision = 20, scale = 10)
	private BigDecimal claimGatherActualInterest;//计划收利息
	@Column(name = "clm_gpint", nullable = false, precision = 12, scale = 2)
	private BigDecimal claimGatherPretendInterest;//计划收利息（四舍五入）
	@Column(name = "clm_gajint", nullable = false, precision = 20, scale = 10)
	private BigDecimal claimGatherActualJusticeInterest;//计划收罚息
	@Column(name = "clm_gpjint", nullable = false, precision = 12, scale = 2)
	private BigDecimal claimGatherPretendJusticeInterest;//计划收罚息（四舍五入）
	@Column(name = "clm_gatotal", nullable = false, precision = 20, scale = 10)
	private BigDecimal claimGatherActualTatolAmount;//计划收总额
	@Column(name = "clm_gptotal", nullable = false, precision = 12, scale = 2)
	private BigDecimal claimGatherPretendTatolAmount;//计划收总额（四舍五入）
	@Column(name = "clm_grapri", nullable = false, precision = 20, scale = 10)
	private BigDecimal claimGatherActualSurplus;//计划剩余本金
	@Column(name = "clm_grppri", nullable = false, precision = 12, scale = 2)
	private BigDecimal claimGatherPretendSurplus;//计划剩余本金（四舍五入）
	@Column(name = "clm_gperiods", nullable = false, length = 20)
	private String claimGatherPeriod;// 收款期数
	@Column(name = "clm_gdate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date claimGatherDate;// 收款时间
	@Column(name = "clm_gtype")
	private Integer claimGatherType;// 收款性质
	@Column(name = "clm_sdate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date claimGatherStartDate;// 起息日
	@Column(name = "t_pacct_seq", nullable = false)
	//付费用户P2P平台账号流水号	
	private Long payAccountSequence;
	@Column(name = "t_gacct_seq", nullable = false)
	//收费用户P2P平台账号流水号	
	private Long gatherAccountSequence;
	@Column(name = "act_clm_amfee", nullable = false, precision = 20, scale = 10)
	//p2p平台投资管理费（四舍五入）	
	private BigDecimal accountClaimActualManageFee;
	@Column(name = "act_clm_pmfee", nullable = false, precision = 12, scale = 2)
	//p2p平台投资管理费	
	private BigDecimal accountClaimShouldManageFee;
	@Column(name = "t_gsacct_seq")
	//vmoney-p2p平台收费账号流水号	
	private Integer gatherServiceAccountSequence;
	@Column(name = "cf_id")
	//汇付资金划拨流水号
	private BigDecimal cashFlowId;

	public Long getClaimGatherRecordSequence() {
		return claimGatherRecordSequence;
	}

	public void setClaimGatherRecordSequence(Long claimGatherRecordSequence) {
		this.claimGatherRecordSequence = claimGatherRecordSequence;
	}

	public Long getAccountClaimSequence() {
		return accountClaimSequence;
	}

	public void setAccountClaimSequence(Long accountClaimSequence) {
		this.accountClaimSequence = accountClaimSequence;
	}

	public Integer getClaimGatherNumber() {
		return claimGatherNumber;
	}

	public void setClaimGatherNumber(Integer claimGatherNumber) {
		this.claimGatherNumber = claimGatherNumber;
	}

	public String getClaimGatherPeriod() {
		return claimGatherPeriod;
	}

	public void setClaimGatherPeriod(String claimGatherPeriod) {
		this.claimGatherPeriod = claimGatherPeriod;
	}

	public Date getClaimGatherDate() {
		return claimGatherDate;
	}

	public void setClaimGatherDate(Date claimGatherDate) {
		this.claimGatherDate = claimGatherDate;
	}

	public Integer getClaimGatherType() {
		return claimGatherType;
	}

	public void setClaimGatherType(Integer claimGatherType) {
		this.claimGatherType = claimGatherType;
	}

	public Date getClaimGatherStartDate() {
		return claimGatherStartDate;
	}

	public void setClaimGatherStartDate(Date claimGatherStartDate) {
		this.claimGatherStartDate = claimGatherStartDate;
	}

	public BigDecimal getClaimGatherActualPrincipal() {
		return claimGatherActualPrincipal;
	}

	public void setClaimGatherActualPrincipal(BigDecimal claimGatherActualPrincipal) {
		this.claimGatherActualPrincipal = claimGatherActualPrincipal;
	}

	public BigDecimal getClaimGatherPretendPrincipal() {
		return claimGatherPretendPrincipal;
	}

	public void setClaimGatherPretendPrincipal(BigDecimal claimGatherPretendPrincipal) {
		this.claimGatherPretendPrincipal = claimGatherPretendPrincipal;
	}

	public BigDecimal getClaimGatherActualInterest() {
		return claimGatherActualInterest;
	}

	public void setClaimGatherActualInterest(BigDecimal claimGatherActualInterest) {
		this.claimGatherActualInterest = claimGatherActualInterest;
	}

	public BigDecimal getClaimGatherPretendInterest() {
		return claimGatherPretendInterest;
	}

	public void setClaimGatherPretendInterest(BigDecimal claimGatherPretendInterest) {
		this.claimGatherPretendInterest = claimGatherPretendInterest;
	}

	public BigDecimal getClaimGatherActualJusticeInterest() {
		return claimGatherActualJusticeInterest;
	}

	public void setClaimGatherActualJusticeInterest(BigDecimal claimGatherActualJusticeInterest) {
		this.claimGatherActualJusticeInterest = claimGatherActualJusticeInterest;
	}

	public BigDecimal getClaimGatherPretendJusticeInterest() {
		return claimGatherPretendJusticeInterest;
	}

	public void setClaimGatherPretendJusticeInterest(BigDecimal claimGatherPretendJusticeInterest) {
		this.claimGatherPretendJusticeInterest = claimGatherPretendJusticeInterest;
	}

	public BigDecimal getClaimGatherActualTatolAmount() {
		return claimGatherActualTatolAmount;
	}

	public void setClaimGatherActualTatolAmount(BigDecimal claimGatherActualTatolAmount) {
		this.claimGatherActualTatolAmount = claimGatherActualTatolAmount;
	}

	public BigDecimal getClaimGatherPretendTatolAmount() {
		return claimGatherPretendTatolAmount;
	}

	public void setClaimGatherPretendTatolAmount(BigDecimal claimGatherPretendTatolAmount) {
		this.claimGatherPretendTatolAmount = claimGatherPretendTatolAmount;
	}

	public BigDecimal getClaimGatherActualSurplus() {
		return claimGatherActualSurplus;
	}

	public void setClaimGatherActualSurplus(BigDecimal claimGatherActualSurplus) {
		this.claimGatherActualSurplus = claimGatherActualSurplus;
	}

	public BigDecimal getClaimGatherPretendSurplus() {
		return claimGatherPretendSurplus;
	}

	public void setClaimGatherPretendSurplus(BigDecimal claimGatherPretendSurplus) {
		this.claimGatherPretendSurplus = claimGatherPretendSurplus;
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

	public BigDecimal getAccountClaimActualManageFee() {
		return accountClaimActualManageFee;
	}

	public void setAccountClaimActualManageFee(BigDecimal accountClaimActualManageFee) {
		this.accountClaimActualManageFee = accountClaimActualManageFee;
	}

	public BigDecimal getAccountClaimShouldManageFee() {
		return accountClaimShouldManageFee;
	}

	public void setAccountClaimShouldManageFee(BigDecimal accountClaimShouldManageFee) {
		this.accountClaimShouldManageFee = accountClaimShouldManageFee;
	}

	public Integer getGatherServiceAccountSequence() {
		return gatherServiceAccountSequence;
	}

	public void setGatherServiceAccountSequence(Integer gatherServiceAccountSequence) {
		this.gatherServiceAccountSequence = gatherServiceAccountSequence;
	}

	public BigDecimal getCashFlowId() {
		return cashFlowId;
	}

	public void setCashFlowId(BigDecimal cashFlowId) {
		this.cashFlowId = cashFlowId;
	}

}
