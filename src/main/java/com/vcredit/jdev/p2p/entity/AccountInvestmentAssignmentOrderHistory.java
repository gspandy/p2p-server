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

//债权转让订单历史
@Entity
@Table(name = "t_act_iaoh")
public class AccountInvestmentAssignmentOrderHistory {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_act_iaoh_seq", unique = true, nullable = false)
	private Long accountInvestmentAssignmentOrderHistorySequence;// 债权转让订单历史流水号
	@Column(name = "t_act_ass_order_seq", nullable = false)
	private Long accountAssignmentOrderSequence;// 项目转让流订单流水号
	@Column(name = "ostat_cdate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderStatusChangeDate;// 状态变化时间
	@Column(name = "order_stat", nullable = false)
	private Integer orderStatus;// 订单状态 常量-参考数据字典
	public Long getAccountInvestmentAssignmentOrderHistorySequence() {
		return accountInvestmentAssignmentOrderHistorySequence;
	}
	public void setAccountInvestmentAssignmentOrderHistorySequence(
			Long accountInvestmentAssignmentOrderHistorySequence) {
		this.accountInvestmentAssignmentOrderHistorySequence = accountInvestmentAssignmentOrderHistorySequence;
	}
	public Long getAccountAssignmentOrderSequence() {
		return accountAssignmentOrderSequence;
	}
	public void setAccountAssignmentOrderSequence(
			Long accountAssignmentOrderSequence) {
		this.accountAssignmentOrderSequence = accountAssignmentOrderSequence;
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
