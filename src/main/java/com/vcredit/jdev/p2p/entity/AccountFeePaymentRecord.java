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


//用户缴费记录
@Entity
@Table(name="t_act_precord")
public class AccountFeePaymentRecord {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_act_precord_seq", unique = true, nullable = false)
	private Long accountPaymentRecordSequence;// 流水号
	@Column(name = "apr_desc",length=60,nullable = false)
	private String paymentDescription;// 缴费描述
	@Column(name = "apr_name",length=20,nullable = false)
	private String paymentName;// 缴费名称
	@Column(name = "apr_pdate",nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date preparePaymentDate;// 应缴时间
	@Column(name = "apr_adate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date actualPaymentDate;// 实际缴费时间
	@Column(name = "apr_gdate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date gatherDate;// 收费用户收款时间
	@Column(name = "apr_stat",nullable = false)
	private Integer gatherStatus;// 缴费状态 常量-参考数据字典
	@Column(name = "apr_amount",nullable = false,precision = 12, scale = 2)
	private BigDecimal paymentAmount;// 缴费金额
	@Column(name = "apr_type",nullable = false)
	private Integer paymentType;// 缴费类别 常量-参考数据字典
	@Column(name = "t_pacct_seq",nullable = false)	//付费用户P2P平台账号流水号	
	private Long payAccountSequence;
	@Column(name = "t_gacct_seq",nullable = false)	//收费用户P2P平台账号流水号	
	private Long gatherAccountSequence;
	@Column(name = "cf_id",length=30)
	//汇付资金划拨流水号
	private String cashFlowId;
	
	public Long getAccountPaymentRecordSequence() {
		return accountPaymentRecordSequence;
	}
	public void setAccountPaymentRecordSequence(Long accountPaymentRecordSequence) {
		this.accountPaymentRecordSequence = accountPaymentRecordSequence;
	}
	public Date getPreparePaymentDate() {
		return preparePaymentDate;
	}
	public void setPreparePaymentDate(Date preparePaymentDate) {
		this.preparePaymentDate = preparePaymentDate;
	}
	public Date getActualPaymentDate() {
		return actualPaymentDate;
	}
	public void setActualPaymentDate(Date actualPaymentDate) {
		this.actualPaymentDate = actualPaymentDate;
	}
	public Date getGatherDate() {
		return gatherDate;
	}
	public void setGatherDate(Date gatherDate) {
		this.gatherDate = gatherDate;
	}
	public Integer getGatherStatus() {
		return gatherStatus;
	}
	public void setGatherStatus(Integer gatherStatus) {
		this.gatherStatus = gatherStatus;
	}
	public BigDecimal getPaymentAmount() {
		return paymentAmount;
	}
	public void setPaymentAmount(BigDecimal paymentAmount) {
		this.paymentAmount = paymentAmount;
	}
	public Integer getPaymentType() {
		return paymentType;
	}
	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}
	public String getPaymentDescription() {
		return paymentDescription;
	}
	public void setPaymentDescription(String paymentDescription) {
		this.paymentDescription = paymentDescription;
	}
	public String getPaymentName() {
		return paymentName;
	}
	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
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
	public String getCashFlowId() {
		return cashFlowId;
	}
	public void setCashFlowId(String cashFlowId) {
		this.cashFlowId = cashFlowId;
	}

}
