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



//用户获得的债权
@Entity
@Table(name = "t_act_clm")
public class AccountClaim {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_act_clm_seq", unique = true, nullable = false)
	private Long accountClaimSequence;// 流水号
	@Column(name = "t_act_inv_seq", nullable = false)
	private Long accountInvestmentSequence;// 用户获得的项目流水号
	@Column(name = "act_clm_gpp", nullable = false)
	private Integer accountClaimPayedPeriod;// 债权收款期数
	//@Column(name = "act_clm_udays", nullable = false)
	//private Integer accountClaimPayedUnknowDays;// 未决天数
	//@Column(name = "act_clm_ddays", nullable = false)
	//private Integer accountClaimPayedDelayDays;// 逾期天数
	@Column(name = "act_clm_status", nullable = false)
	private Integer accountClaimStatus;// 债权状态 常量-参考数据字典
	@Column(name = "act_clm_cdate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date accountClaimStatusChangeDate;// 债权状态变化时间
	@Column(name = "act_clm_pqt", nullable = false, precision = 12, scale = 2)//债权额度（四舍五入）
	private BigDecimal accountClaimPretendQuota;
	@Column(name = "act_clm_aqt", nullable = false,precision = 20, scale = 10)//债权额度
	private BigDecimal accountClaimActualQuota;
	@Column(name = "act_clm_edate")	//债权结清时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date accountClaimEndDate;
	@Column(name = "rec_edate")	//记录更新时间	
	@Temporal(TemporalType.TIMESTAMP)
	private Date recordEditDate;	

	
	public Long getAccountClaimSequence() {
		return accountClaimSequence;
	}
	public void setAccountClaimSequence(Long accountClaimSequence) {
		this.accountClaimSequence = accountClaimSequence;
	}
	public Long getAccountInvestmentSequence() {
		return accountInvestmentSequence;
	}
	public void setAccountInvestmentSequence(Long accountInvestmentSequence) {
		this.accountInvestmentSequence = accountInvestmentSequence;
	}
	public Integer getAccountClaimPayedPeriod() {
		return accountClaimPayedPeriod;
	}
	public void setAccountClaimPayedPeriod(Integer accountClaimPayedPeriod) {
		this.accountClaimPayedPeriod = accountClaimPayedPeriod;
	}
	public Integer getAccountClaimStatus() {
		return accountClaimStatus;
	}
	public void setAccountClaimStatus(Integer accountClaimStatus) {
		this.accountClaimStatus = accountClaimStatus;
	}
	public Date getAccountClaimStatusChangeDate() {
		return accountClaimStatusChangeDate;
	}
	public void setAccountClaimStatusChangeDate(Date accountClaimStatusChangeDate) {
		this.accountClaimStatusChangeDate = accountClaimStatusChangeDate;
	}
	public BigDecimal getAccountClaimPretendQuota() {
		return accountClaimPretendQuota;
	}
	public void setAccountClaimPretendQuota(BigDecimal accountClaimPretendQuota) {
		this.accountClaimPretendQuota = accountClaimPretendQuota;
	}
	public BigDecimal getAccountClaimActualQuota() {
		return accountClaimActualQuota;
	}
	public void setAccountClaimActualQuota(BigDecimal accountClaimActualQuota) {
		this.accountClaimActualQuota = accountClaimActualQuota;
	}
	public Date getAccountClaimEndDate() {
		return accountClaimEndDate;
	}
	public void setAccountClaimEndDate(Date accountClaimEndDate) {
		this.accountClaimEndDate = accountClaimEndDate;
	}
	public Date getRecordEditDate() {
		return recordEditDate;
	}
	public void setRecordEditDate(Date recordEditDate) {
		this.recordEditDate = recordEditDate;
	}
	
}
