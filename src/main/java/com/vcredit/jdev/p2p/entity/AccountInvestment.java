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



//用户获得的项目
@Entity
@Table(name = "t_act_inv")
public class AccountInvestment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_act_inv_seq", unique = true, nullable = false)
	private Long accountInvestmentSequence;// 流水号
	@Column(name = "t_inv_seq", nullable = false)
	private Long investmentSequence;// 投资项目流水号
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
	@Column(name = "act_inv_gform", nullable = false)//用户获得项目的方式 常量-参考数据字典
	private Integer accountGetInvestmentForm;
	@Column(name = "act_inv_num", nullable = false,length=60)	//用户获得的项目编号	
	private String accountInvestmentNumber;
	@Column(name = "act_inv_eform")	//结清方式	
	private Integer accountInvestmentEndForm;
	@Column(name = "t_acct_fseq", nullable = false)	//用户获得的项目原所有者	
	private Long accountFirstSequence;
	@Column(name = "act_inv_cont_id",length=60)//	合同编号	
	private String accountInvestmentContractId;
	@Column(name = "rec_edate")	//记录更新时间	
	@Temporal(TemporalType.TIMESTAMP)
	private Date recordEditDate;



	public Date getRecordEditDate() {
		return recordEditDate;
	}
	public void setRecordEditDate(Date recordEditDate) {
		this.recordEditDate = recordEditDate;
	}
	public Long getInvestmentSequence() {
		return investmentSequence;
	}
	public void setInvestmentSequence(Long investmentSequence) {
		this.investmentSequence = investmentSequence;
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
	public Integer getAccountGetInvestmentForm() {
		return accountGetInvestmentForm;
	}
	public void setAccountGetInvestmentForm(Integer accountGetInvestmentForm) {
		this.accountGetInvestmentForm = accountGetInvestmentForm;
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
	public Date getAccountInvestmentEndDate() {
		return accountInvestmentEndDate;
	}
	public void setAccountInvestmentEndDate(Date accountInvestmentEndDate) {
		this.accountInvestmentEndDate = accountInvestmentEndDate;
	}
	public Long getAccountInvestmentSequence() {
		return accountInvestmentSequence;
	}
	public void setAccountInvestmentSequence(Long accountInvestmentSequence) {
		this.accountInvestmentSequence = accountInvestmentSequence;
	}
	public String getAccountInvestmentContractId() {
		return accountInvestmentContractId;
	}
	public void setAccountInvestmentContractId(String accountInvestmentContractId) {
		this.accountInvestmentContractId = accountInvestmentContractId;
	}

}
