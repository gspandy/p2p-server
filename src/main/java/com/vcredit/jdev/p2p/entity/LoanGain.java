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



//投资项目放款情况
@Entity
@Table(name="t_loan_gain")
public class LoanGain {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_loan_gain_seq", unique = true, nullable = false)
	private Long loanGainSequence;// 流水号
	@Column(name = "t_inv_seq",nullable = false)
	private Integer investmentSequence;// 项目编号
	@Column(name = "act_gthird",nullable = false,length=60)
	private String gatherThirdPaymentId;// 贷款用户第三方支付账号gather_account_third-party_payment_id
	@Column(name = "act_pthird",nullable = false,length=60)
	private String payThirdPaymentId;// 投资用户第三方支付账号pay_account_third-party_payment_id
	@Column(name = "lg_pamount",nullable = false,precision = 12, scale = 2)
	private BigDecimal loanGainPlanAmount;// 放款金额
	@Column(name = "lg_tamount",nullable = false,precision = 12, scale = 2)
	private BigDecimal loanGainTrueAmount;// 实际发放金额
	@Column(name = "lg_pdate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date loanGainPlanDate;// 发放时间
	@Column(name = "lg_tdate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date loanGainTrueDate;// 到账时间
	@Column(name = "lg_stat",nullable = false)
	private Integer loanGainStatus;// 放款状态
	@Column(name = "lg_sdesc",length=60)
	private String loanGainStatusDescription;// 放款状态说明
	@Column(name = "lg_gfee",nullable = false,precision = 12, scale = 2)
	private BigDecimal loanGainFee;// p2p平台放款手续费
	public Long getLoanGainSequence() {
		return loanGainSequence;
	}
	public void setLoanGainSequence(Long loanGainSequence) {
		this.loanGainSequence = loanGainSequence;
	}
	public Integer getInvestmentSequence() {
		return investmentSequence;
	}
	public void setInvestmentSequence(Integer investmentSequence) {
		this.investmentSequence = investmentSequence;
	}
	public BigDecimal getLoanGainPlanAmount() {
		return loanGainPlanAmount;
	}
	public void setLoanGainPlanAmount(BigDecimal loanGainPlanAmount) {
		this.loanGainPlanAmount = loanGainPlanAmount;
	}
	public BigDecimal getLoanGainTrueAmount() {
		return loanGainTrueAmount;
	}
	public void setLoanGainTrueAmount(BigDecimal loanGainTrueAmount) {
		this.loanGainTrueAmount = loanGainTrueAmount;
	}
	public Date getLoanGainTrueDate() {
		return loanGainTrueDate;
	}
	public void setLoanGainTrueDate(Date loanGainTrueDate) {
		this.loanGainTrueDate = loanGainTrueDate;
	}
	public Integer getLoanGainStatus() {
		return loanGainStatus;
	}
	public void setLoanGainStatus(Integer loanGainStatus) {
		this.loanGainStatus = loanGainStatus;
	}
	public String getLoanGainStatusDescription() {
		return loanGainStatusDescription;
	}
	public void setLoanGainStatusDescription(String loanGainStatusDescription) {
		this.loanGainStatusDescription = loanGainStatusDescription;
	}
	public BigDecimal getLoanGainFee() {
		return loanGainFee;
	}
	public void setLoanGainFee(BigDecimal loanGainFee) {
		this.loanGainFee = loanGainFee;
	}
	public String getGatherThirdPaymentId() {
		return gatherThirdPaymentId;
	}
	public void setGatherThirdPaymentId(String gatherThirdPaymentId) {
		this.gatherThirdPaymentId = gatherThirdPaymentId;
	}
	public String getPayThirdPaymentId() {
		return payThirdPaymentId;
	}
	public void setPayThirdPaymentId(String payThirdPaymentId) {
		this.payThirdPaymentId = payThirdPaymentId;
	}
	public Date getLoanGainPlanDate() {
		return loanGainPlanDate;
	}
	public void setLoanGainPlanDate(Date loanGainPlanDate) {
		this.loanGainPlanDate = loanGainPlanDate;
	}

}
