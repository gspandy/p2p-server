package com.vcredit.jdev.p2p.dto;

import java.util.Date;

/**
 * 用户Dto
 * 
 * @author zhuqiu
 *
 */
public class DealDetailListDto {

	public DealDetailListDto(Date tradeDate, String tradeType, String orderId, Double tradeAmount, Double balance, String target,
			String tradeDescription) {
		super();
		this.tradeDate = tradeDate;
		this.tradeType = tradeType;
		this.orderId = orderId;
		this.tradeAmount = tradeAmount;
		this.balance = balance;
		this.target = target;
		this.tradeDescription = tradeDescription;
	}

	/** 订单交易时间 */
	private Date tradeDate;
	/** '网银充值' as ,交易类型(中文) */
	private String tradeType;
	/** 订单号 */
	private String orderId;
	/** 交易金额 */
	private Double tradeAmount;
	/** 可用余额 */
	private Double balance;
	/** 交易对象 */
	private String target;
	/** #备注 */
	private String tradeDescription;

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Double getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(Double tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTradeDescription() {
		return tradeDescription;
	}

	public void setTradeDescription(String tradeDescription) {
		this.tradeDescription = tradeDescription;
	}

}
