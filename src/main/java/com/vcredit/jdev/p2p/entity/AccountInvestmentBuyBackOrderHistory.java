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

//用户回购订单历史
@Entity
@Table(name = "t_act_iboh")
public class AccountInvestmentBuyBackOrderHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_act_iboh_seq", unique = true, nullable = false)
	private Long accountInvestmentBuyBackOrderHistorySequence;// 垫付订单历史流水号
	@Column(name = "t_act_bb_order_seq", nullable = false)
	private Long accountBuyBackOrderSequence;// 项目回购流订单流水号
	@Column(name = "ostat_cdate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderStatusChangeDate;// 状态变化时间
	@Column(name = "order_stat", nullable = false)
	private Integer orderStatus;// 订单状态 常量-参考数据字典
	public Long getAccountInvestmentBuyBackOrderHistorySequence() {
		return accountInvestmentBuyBackOrderHistorySequence;
	}
	public void setAccountInvestmentBuyBackOrderHistorySequence(
			Long accountInvestmentBuyBackOrderHistorySequence) {
		this.accountInvestmentBuyBackOrderHistorySequence = accountInvestmentBuyBackOrderHistorySequence;
	}
	public Long getAccountBuyBackOrderSequence() {
		return accountBuyBackOrderSequence;
	}
	public void setAccountBuyBackOrderSequence(Long accountBuyBackOrderSequence) {
		this.accountBuyBackOrderSequence = accountBuyBackOrderSequence;
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


}
