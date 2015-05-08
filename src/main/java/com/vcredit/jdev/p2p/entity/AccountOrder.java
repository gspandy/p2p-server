package com.vcredit.jdev.p2p.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.ColumnResult;
import javax.persistence.ConstructorResult;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SqlResultSetMapping;
import javax.persistence.SqlResultSetMappings;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.vcredit.jdev.p2p.dto.DealDetailListDto;

//用户订单
@SqlResultSetMappings({ @SqlResultSetMapping(name = "DealDetailListDtoMapping", classes = { @ConstructorResult(targetClass = DealDetailListDto.class, columns = {
		@ColumnResult(name = "tradeDate", type = Date.class), @ColumnResult(name = "tradeType"), @ColumnResult(name = "orderId"),
		@ColumnResult(name = "tradeAmount", type = Double.class), @ColumnResult(name = "balance", type = Double.class),
		@ColumnResult(name = "target"), @ColumnResult(name = "tradeDesc") }) }) })
@Entity
@Table(name = "t_act_order")
public class AccountOrder {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "t_act_order_seq", unique = true, nullable = false)
	private Long accountOrderSequence;// 流水号
	@Column(name = "trade_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date tradeDate;// 交易时间
	@Column(name = "order_stat", nullable = false)
	private Integer orderStatus;// 订单状态 常量-参考数据字典
	@Column(name = "trade_type", nullable = false)
	private Integer tradeType;// 交易类型 常量-参考数据字典
	@Column(name = "trade_amount", nullable = false, precision = 12, scale = 2)
	private BigDecimal tradeAmount;// 交易金额
	@Column(name = "trade_ctpvalue")
	private Long commodityTablePrimaryKeyValue;// 交易商品表主键值
	@Column(name = "t_pacct_seq", nullable = false)
	//付费用户P2P平台账号流水号	
	private Long payAccountSequence;
	@Column(name = "t_gacct_seq", nullable = false)
	//收费用户P2P平台账号流水号	
	private Long gatherAccountSequence;
	@Column(name = "cf_id")
	//汇付资金划拨流水号
	private String cashFlowId;
	@Column(name = "act_pthblc", nullable = false, precision = 12, scale = 2)
	//付款人账号余额	
	private BigDecimal payerThirdPaymentIdBalance;
	@Column(name = "act_gthblc", nullable = false, precision = 12, scale = 2)
	//收款人账号余额	
	private BigDecimal gatherThirdPaymentIdBalance;
	@Column(name = "trade_desc", length = 90)
	//交易描述	
	private String tradeDescription;
	@Column(name = "trxid")
	private BigDecimal trxId;//汇付交易唯一标识
	@Column(name = "order_edate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderEditDate;// 订单更新时间
	@Column(name = "trade_comm", length = 255)
	//交易注释	
	private String tradeComment;	
	@Column(name = "rec_status",nullable = false)
	private Integer recordStatus;//记录状态 常量-参考数据字典

	public Integer getRecordStatus() {
		return recordStatus;
	}

	public void setRecordStatus(Integer recordStatus) {
		this.recordStatus = recordStatus;
	}

	public Long getAccountOrderSequence() {
		return accountOrderSequence;
	}

	public void setAccountOrderSequence(Long accountOrderSequence) {
		this.accountOrderSequence = accountOrderSequence;
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

	public Integer getTradeType() {
		return tradeType;
	}

	public void setTradeType(Integer tradeType) {
		this.tradeType = tradeType;
	}

	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public void setCommodityTablePrimaryKeyValue(Long commodityTablePrimaryKeyValue) {
		this.commodityTablePrimaryKeyValue = commodityTablePrimaryKeyValue;
	}

	public Long getCommodityTablePrimaryKeyValue() {
		return commodityTablePrimaryKeyValue;
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

	public BigDecimal getPayerThirdPaymentIdBalance() {
		return payerThirdPaymentIdBalance;
	}

	public void setPayerThirdPaymentIdBalance(BigDecimal payerThirdPaymentIdBalance) {
		this.payerThirdPaymentIdBalance = payerThirdPaymentIdBalance;
	}

	public BigDecimal getGatherThirdPaymentIdBalance() {
		return gatherThirdPaymentIdBalance;
	}

	public void setGatherThirdPaymentIdBalance(BigDecimal gatherThirdPaymentIdBalance) {
		this.gatherThirdPaymentIdBalance = gatherThirdPaymentIdBalance;
	}

	public Date getOrderEditDate() {
		return orderEditDate;
	}

	public void setOrderEditDate(Date orderEditDate) {
		this.orderEditDate = orderEditDate;
	}

	public String getTradeComment() {
		return tradeComment;
	}

	public void setTradeComment(String tradeComment) {
		this.tradeComment = tradeComment;
	}

	public String getCashFlowId() {
		return cashFlowId;
	}

	public void setCashFlowId(String cashFlowId) {
		this.cashFlowId = cashFlowId;
	}

}
