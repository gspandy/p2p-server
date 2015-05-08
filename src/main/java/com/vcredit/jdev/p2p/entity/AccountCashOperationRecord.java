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


//用户充值/提现记录
@Entity
@Table(name="t_act_rdrcd")
public class AccountCashOperationRecord {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_act_rdrcd_seq", unique = true, nullable = false)
	private	Long	cashOperationRecordSequence	;//	流水号	account_recharge/draw_record_sequence
	@Column(name = "ard_desc",length=60,nullable = false)
	private	String	operateDescription	;//	操作描述	operate_description
	@Column(name = "ard_rdate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private	Date	operateDate	;//	操作时间	operate_date
	@Column(name = "ard_dpfee",nullable = false,precision = 12, scale = 2)
	private	BigDecimal	drawP2pFee	;//	p2p平台取现手续费	operate_p2p_fee
	@Column(name = "ard_gmoney",nullable = false,precision = 12, scale = 2)
	private	BigDecimal	operateMoney	;//	充值/提现金额	operate_money
	@Column(name = "ard_stat",nullable = false)
	private	Integer	operateStats	;//	操作状态	operate_stats 常量-参考数据字典
	@Column(name = "ard_type",nullable = false)
	private	Integer	operateType	;//	操作类型	operate_type 常量-参考数据字典
	@Column(name = "ard_tfee",precision = 12, scale = 2)
	private	BigDecimal	operateThirdPaymentFee	;//	第三方支付平台手续费	draw_third-party_payment_fee
	@Column(name = "bank_code",nullable = false)
	private	Integer	bankCode	;//	所属银行	bank_code 常量-参考数据字典
	@Column(name = "abc_num",nullable = false,length=30)
	private	String	bankCardNumber	;//	银行卡号码	bank_card_number
	@Column(name = "t_acct_seq",nullable = false)
	private Long accountSequence;// p2p平台用户流水号 account_sequence
	@Column(name = "t_dsacct_seq")	//vmoney-提现服务p2p平台账号	
	private Long drawServiceAccountSequence;
	@Column(name = "t_tacct_seq")//	第三方支付p2p平台账号	
	private Long thirdPaymentAccountSequence;
	@Column(name = "cf_id",length=30)
	//汇付资金划拨流水号
	private String cashFlowId;
	
	public Long getCashOperationRecordSequence() {
		return cashOperationRecordSequence;
	}
	public void setCashOperationRecordSequence(Long cashOperationRecordSequence) {
		this.cashOperationRecordSequence = cashOperationRecordSequence;
	}
	public String getOperateDescription() {
		return operateDescription;
	}
	public void setOperateDescription(String operateDescription) {
		this.operateDescription = operateDescription;
	}
	public Date getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}
	public BigDecimal getOperateMoney() {
		return operateMoney;
	}
	public void setOperateMoney(BigDecimal operateMoney) {
		this.operateMoney = operateMoney;
	}
	public Integer getOperateStats() {
		return operateStats;
	}
	public void setOperateStats(Integer operateStats) {
		this.operateStats = operateStats;
	}
	public Integer getOperateType() {
		return operateType;
	}
	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}
	public BigDecimal getOperateThirdPaymentFee() {
		return operateThirdPaymentFee;
	}
	public void setOperateThirdPaymentFee(BigDecimal operateThirdPaymentFee) {
		this.operateThirdPaymentFee = operateThirdPaymentFee;
	}
	public Integer getBankCode() {
		return bankCode;
	}
	public void setBankCode(Integer bankCode) {
		this.bankCode = bankCode;
	}
	public Long getAccountSequence() {
		return accountSequence;
	}
	public void setAccountSequence(Long accountSequence) {
		this.accountSequence = accountSequence;
	}
	public BigDecimal getDrawP2pFee() {
		return drawP2pFee;
	}
	public void setDrawP2pFee(BigDecimal drawP2pFee) {
		this.drawP2pFee = drawP2pFee;
	}
	public Long getDrawServiceAccountSequence() {
		return drawServiceAccountSequence;
	}
	public void setDrawServiceAccountSequence(Long drawServiceAccountSequence) {
		this.drawServiceAccountSequence = drawServiceAccountSequence;
	}
	public Long getThirdPaymentAccountSequence() {
		return thirdPaymentAccountSequence;
	}
	public void setThirdPaymentAccountSequence(Long thirdPaymentAccountSequence) {
		this.thirdPaymentAccountSequence = thirdPaymentAccountSequence;
	}
	public String getBankCardNumber() {
		return bankCardNumber;
	}
	public void setBankCardNumber(String bankCardNumber) {
		this.bankCardNumber = bankCardNumber;
	}
	public String getCashFlowId() {
		return cashFlowId;
	}
	public void setCashFlowId(String cashFlowId) {
		this.cashFlowId = cashFlowId;
	}

}
