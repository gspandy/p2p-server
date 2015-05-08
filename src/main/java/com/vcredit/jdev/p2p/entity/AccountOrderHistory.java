package com.vcredit.jdev.p2p.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



//用户订单历史
@Entity
@Table(name = "t_act_oh")
public class AccountOrderHistory {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_act_oh_seq", unique = true, nullable = false)
	private Long accountOrderHistorySequence;// 用户订单状态历史流水号
	@Column(name = "t_act_order_seq", nullable = false)
	private Long accountOrderSequence;// 用户订单流水号
	@Column(name = "ostat_cdate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderStatusChangeDate;// 状态变化时间
	@Column(name = "order_stat", nullable = false)
	private Integer orderStatus;// 订单状态 常量-参考数据字典
	@Column(name = "trade_desc",length=90)	//交易描述	
	private String tradeDescription;
	public Long getAccountOrderHistorySequence() {
		return accountOrderHistorySequence;
	}
	public void setAccountOrderHistorySequence(Long accountOrderHistorySequence) {
		this.accountOrderHistorySequence = accountOrderHistorySequence;
	}
	public Long getAccountOrderSequence() {
		return accountOrderSequence;
	}
	public void setAccountOrderSequence(Long accountOrderSequence) {
		this.accountOrderSequence = accountOrderSequence;
	}
	public Date getOrderStatusChangeDate() {
		return orderStatusChangeDate;
	}
	public void setOrderStatusChangeDate(Date orderStatusChangeDate) {
		this.orderStatusChangeDate = orderStatusChangeDate;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getTradeDescription() {
		return tradeDescription;
	}
	public void setTradeDescription(String tradeDescription) {
		this.tradeDescription = tradeDescription;
	}

}
