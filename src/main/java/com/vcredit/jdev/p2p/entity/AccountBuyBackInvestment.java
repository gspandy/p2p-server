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
@Table(name = "t_act_bb_inv")//用户项目回购记录
public class AccountBuyBackInvestment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_act_bb_inv_seq", unique = true, nullable = false)
	private Long accountBuyBackInvestmentSequence;
	@Column(name = "t_act_inv_seq",nullable = false)
	private Long accountInvestmentSequence;// 用户获得项目的流水号
	@Column(name = "act_inv_qt", nullable = false,precision = 12, scale = 2)
	private BigDecimal accountInvestmentQuota;// 投资额度
	@Column(name = "act_inv_cdate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date accountInvestmentIssueDate;// 用户获得的项目投资状态变化时间
	@Column(name = "act_inv_gpp", nullable = false)
	private Integer accountInvestmentPayedPeriod;// 已收款（还款）期数
	@Column(name = "act_inv_edate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date accountInvestmentEndDate;// 项目结清时间
	@Column(name = "act_inv_stat", nullable = false)
	private Integer accountInvestmentStatus;// 用户获得的项目状态 常量-参考数据字典
	@Column(name = "t_acct_seq", nullable = false)	//用户P2P平台账号流水号	
	private Long accountSequence;
	@Column(name = "act_inv_num", nullable = false,length=60)	//用户获得的项目编号	
	private String accountInvestmentNumber;
	@Column(name = "act_inv_eform")	//结清方式	
	private Integer accountInvestmentEndForm;
	@Column(name = "t_acct_fseq", nullable = false)	//用户获得的项目原所有者	
	private Long accountFirstSequence;
	public Long getAccountBuyBackInvestmentSequence() {
		return accountBuyBackInvestmentSequence;
	}
	public void setAccountBuyBackInvestmentSequence(
			Long accountBuyBackInvestmentSequence) {
		this.accountBuyBackInvestmentSequence = accountBuyBackInvestmentSequence;
	}
	public Long getAccountInvestmentSequence() {
		return accountInvestmentSequence;
	}
	public void setAccountInvestmentSequence(Long accountInvestmentSequence) {
		this.accountInvestmentSequence = accountInvestmentSequence;
	}
	public BigDecimal getAccountInvestmentQuota() {
		return accountInvestmentQuota;
	}
	public void setAccountInvestmentQuota(BigDecimal accountInvestmentQuota) {
		this.accountInvestmentQuota = accountInvestmentQuota;
	}
	public Date getAccountInvestmentIssueDate() {
		return accountInvestmentIssueDate;
	}
	public void setAccountInvestmentIssueDate(Date accountInvestmentIssueDate) {
		this.accountInvestmentIssueDate = accountInvestmentIssueDate;
	}
	public Integer getAccountInvestmentPayedPeriod() {
		return accountInvestmentPayedPeriod;
	}
	public void setAccountInvestmentPayedPeriod(Integer accountInvestmentPayedPeriod) {
		this.accountInvestmentPayedPeriod = accountInvestmentPayedPeriod;
	}
	public Date getAccountInvestmentEndDate() {
		return accountInvestmentEndDate;
	}
	public void setAccountInvestmentEndDate(Date accountInvestmentEndDate) {
		this.accountInvestmentEndDate = accountInvestmentEndDate;
	}
	public Integer getAccountInvestmentStatus() {
		return accountInvestmentStatus;
	}
	public void setAccountInvestmentStatus(Integer accountInvestmentStatus) {
		this.accountInvestmentStatus = accountInvestmentStatus;
	}
	public Long getAccountSequence() {
		return accountSequence;
	}
	public void setAccountSequence(Long accountSequence) {
		this.accountSequence = accountSequence;
	}
	public String getAccountInvestmentNumber() {
		return accountInvestmentNumber;
	}
	public void setAccountInvestmentNumber(String accountInvestmentNumber) {
		this.accountInvestmentNumber = accountInvestmentNumber;
	}
	public Integer getAccountInvestmentEndForm() {
		return accountInvestmentEndForm;
	}
	public void setAccountInvestmentEndForm(Integer accountInvestmentEndForm) {
		this.accountInvestmentEndForm = accountInvestmentEndForm;
	}
	public Long getAccountFirstSequence() {
		return accountFirstSequence;
	}
	public void setAccountFirstSequence(Long accountFirstSequence) {
		this.accountFirstSequence = accountFirstSequence;
	}
	
}
