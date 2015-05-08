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

@Entity
@Table(name = "t_act_spot_clm")//债权垫付记录
public class AccountSpotClaim {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_act_spot_clm_seq", unique = true, nullable = false)	//流水号	
	private Long accountSpotClaimSequence;
	@Column(name = "act_clm_pqt", nullable = false)	//债权额度（四舍五入）	
	private BigDecimal accountClaimPretendQuota;
	@Column(name = "act_clm_aqt", nullable = false)	//债权额度	
	private BigDecimal accountClaimActualQuota;
	@Column(name = "act_clm_gpp", nullable = false)	//债权收款期数	
	private Integer accountClaimPayedPeriod;
	@Column(name = "act_clm_status", nullable = false)	//债权状态	
	private Integer accountClaimStatus;
	@Column(name = "t_act_clm_seq", nullable = false)	//用户获得的债权流水号	
	private Long accountClaimSequence;
	@Column(name = "t_acct_seq", nullable = false)	//用户p2p平台账号流水号	
	private Long accountSequence;
	@Column(name = "actClmCdate", nullable = false)	//债权获得时间	
	@Temporal(TemporalType.TIMESTAMP)
	private Date accountClaimCreateDate;
	@Column(name = "act_clm_edate")	//债权结清时间
	@Temporal(TemporalType.TIMESTAMP)
	private Date accountClaimEndDate;
	
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
	public Long getAccountClaimSequence() {
		return accountClaimSequence;
	}
	public void setAccountClaimSequence(Long accountClaimSequence) {
		this.accountClaimSequence = accountClaimSequence;
	}
	public Long getAccountSequence() {
		return accountSequence;
	}
	public void setAccountSequence(Long accountSequence) {
		this.accountSequence = accountSequence;
	}
	public Date getAccountClaimCreateDate() {
		return accountClaimCreateDate;
	}
	public void setAccountClaimCreateDate(Date accountClaimCreateDate) {
		this.accountClaimCreateDate = accountClaimCreateDate;
	}
	public Date getAccountClaimEndDate() {
		return accountClaimEndDate;
	}
	public void setAccountClaimEndDate(Date accountClaimEndDate) {
		this.accountClaimEndDate = accountClaimEndDate;
	}
	public Long getAccountSpotClaimSequence() {
		return accountSpotClaimSequence;
	}
	public void setAccountSpotClaimSequence(Long accountSpotClaimSequence) {
		this.accountSpotClaimSequence = accountSpotClaimSequence;
	}

}
