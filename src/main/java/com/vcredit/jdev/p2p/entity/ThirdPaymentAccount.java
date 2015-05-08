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



/**
 * 第三方支付账号
 * */
@Entity
@Table(name="t_act_third")
public class ThirdPaymentAccount {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_act_third_seq", unique = true, nullable = false)
	private Long thirdPaymentSequence;// 第三方支付账号流水号 third_payment_sequence
	
	@Column(name="t_acct_seq",nullable=false)
	private Long accountSequence;// 用户P2P平台账号流水号 account_sequence
	
	@Column(name="act_third",nullable=false,length=60)
	private String thirdPaymentId;// 第三方支付账号 account_third-party_payment_id
	
	@Column(name="act_thbstat")
	private Integer thirdPaymentIdBindStatus;// 账号绑定状态account_third-party_payment_id_bind_status 常量-参考数据字典
	
	@Column(name="act_thastat")
	private Integer thirdPaymentIdActiveStatus;// 账号激活状态account_third-party_payment_id_active_status 常量-参考数据字典
	
	@Column(name="act_thblc",precision = 12, scale = 2)
	private BigDecimal thirdPaymentIdBalance;// 账号总额account_third-party_payment_id_balance
	
	@Column(name="act_thtype",nullable=false)
	private Integer thirdPaymentIdType;// 账号类型account_third-party_payment_id_type 常量-参考数据字典
	
	@Column(name="act_thrdate",nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date thirdPaymentIdRegisterDate;// 注册时间account_third-party_payment_id_register_date
	@Column(name="act_thuid")	//第三方账号唯一标识	
	private Long thirdPaymentUniqueId;
	
	@Column(name="act_thvblc",precision = 12, scale = 2)
	private BigDecimal thirdPaymentValidBalance;// 账号可用额account_third-party_payment_Valid_balance
	
	@Column(name = "rec_odate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date recordOperateDate;//记录处理时间	
	
	public BigDecimal getThirdPaymentValidBalance() {
		return thirdPaymentValidBalance;
	}
	public void setThirdPaymentValidBalance(BigDecimal thirdPaymentValidBalance) {
		this.thirdPaymentValidBalance = thirdPaymentValidBalance;
	}
	public Date getRecordOperateDate() {
		return recordOperateDate;
	}
	public void setRecordOperateDate(Date recordOperateDate) {
		this.recordOperateDate = recordOperateDate;
	}
	public Long getThirdPaymentSequence() {
		return thirdPaymentSequence;
	}
	public void setThirdPaymentSequence(Long thirdPaymentSequence) {
		this.thirdPaymentSequence = thirdPaymentSequence;
	}
	public Integer getThirdPaymentIdBindStatus() {
		return thirdPaymentIdBindStatus;
	}
	public void setThirdPaymentIdBindStatus(Integer thirdPaymentIdBindStatus) {
		this.thirdPaymentIdBindStatus = thirdPaymentIdBindStatus;
	}
	public Integer getThirdPaymentIdActiveStatus() {
		return thirdPaymentIdActiveStatus;
	}
	public void setThirdPaymentIdActiveStatus(Integer thirdPaymentIdActiveStatus) {
		this.thirdPaymentIdActiveStatus = thirdPaymentIdActiveStatus;
	}
	public BigDecimal getThirdPaymentIdBalance() {
		return thirdPaymentIdBalance;
	}
	public void setThirdPaymentIdBalance(BigDecimal thirdPaymentIdBalance) {
		this.thirdPaymentIdBalance = thirdPaymentIdBalance;
	}
	public Integer getThirdPaymentIdType() {
		return thirdPaymentIdType;
	}
	public void setThirdPaymentIdType(Integer thirdPaymentIdType) {
		this.thirdPaymentIdType = thirdPaymentIdType;
	}
	public Date getThirdPaymentIdRegisterDate() {
		return thirdPaymentIdRegisterDate;
	}
	public void setThirdPaymentIdRegisterDate(Date thirdPaymentIdRegisterDate) {
		this.thirdPaymentIdRegisterDate = thirdPaymentIdRegisterDate;
	}
	public Long getAccountSequence() {
		return accountSequence;
	}
	public void setAccountSequence(Long accountSequence) {
		this.accountSequence = accountSequence;
	}
	public String getThirdPaymentId() {
		return thirdPaymentId;
	}
	public void setThirdPaymentId(String thirdPaymentId) {
		this.thirdPaymentId = thirdPaymentId;
	}
	public Long getThirdPaymentUniqueId() {
		return thirdPaymentUniqueId;
	}
	public void setThirdPaymentUniqueId(Long thirdPaymentUniqueId) {
		this.thirdPaymentUniqueId = thirdPaymentUniqueId;
	}
	
}
