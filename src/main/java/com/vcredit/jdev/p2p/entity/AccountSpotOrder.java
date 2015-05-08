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
@Table(name = "t_act_spot_order")//用户垫付订单
public class AccountSpotOrder {
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_act_spot_order_seq", unique = true, nullable = false)
	private Long accountSpotOrderSequence;// 流水号
	@Column(name = "trade_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date tradeDate;// 交易时间
	@Column(name = "order_stat", nullable = false)
	private Integer orderStatus;// 订单状态 常量-参考数据字典
	@Column(name = "trade_amount", nullable = false)
	private BigDecimal tradeAmount;// 交易金额
	@Column(name = "t_act_clm_seq")
	private Long accountClaimSequence;// 用户获得的债权流水号
	@Column(name = "t_pacct_seq", nullable = false)	//付费用户P2P平台账号流水号	
	private Long payAccountSequence;
	@Column(name = "t_gacct_seq", nullable = false)	//收费用户P2P平台账号流水号	
	private Long gatherAccountSequence;
	@Column(name = "order_id", nullable = false)	//订单号
	private BigDecimal orderId;
	@Column(name = "act_thblc", nullable = false)	//账号余额	
	private BigDecimal thirdPaymentIdBalance;
	@Column(name = "trade_desc",length=90)	//交易描述	
	private String tradeDescription;
	@Column(name = "trxid",length=20)
	private BigDecimal trxId;//汇付交易唯一标识
	public Long getAccountSpotOrderSequence() {
		return accountSpotOrderSequence;
	}
	public void setAccountSpotOrderSequence(Long accountSpotOrderSequence) {
		this.accountSpotOrderSequence = accountSpotOrderSequence;
	}
	public Date getTradeDate() {
		return tradeDate;
	}
	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	public Long getAccountClaimSequence() {
		return accountClaimSequence;
	}
	public void setAccountClaimSequence(Long accountClaimSequence) {
		this.accountClaimSequence = accountClaimSequence;
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
	public BigDecimal getOrderId() {
		return orderId;
	}
	public void setOrderId(BigDecimal orderId) {
		this.orderId = orderId;
	}
	public BigDecimal getThirdPaymentIdBalance() {
		return thirdPaymentIdBalance;
	}
	public void setThirdPaymentIdBalance(BigDecimal thirdPaymentIdBalance) {
		this.thirdPaymentIdBalance = thirdPaymentIdBalance;
	}
	public String getTradeDescription() {
		return tradeDescription;
	}
	public void setTradeDescription(String tradeDescription) {
		this.tradeDescription = tradeDescription;
	}
	public BigDecimal getTrxId() {
		return trxId;
	}
	public void setTrxId(BigDecimal trxId) {
		this.trxId = trxId;
	}
	
}
