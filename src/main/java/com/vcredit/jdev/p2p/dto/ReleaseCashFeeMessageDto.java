package com.vcredit.jdev.p2p.dto;

import java.math.BigDecimal;

/**
 * 提现消息数据类
 * 
 * @author zhuqiu
 *
 */
public class ReleaseCashFeeMessageDto {

	/** 处理结果 */
	private boolean result;
	/** 信息 */
	private String msg;
	/** 项目流水号 */
	private Long investmentSequence;

	//	/** 用户充值/提现记录 流水号 */
	//	private Long cashOperationRecordSequence;//	流水号

	/**
	 * @return the accountOrderSequence
	 */
	public Long getAccountOrderSequence() {
		return accountOrderSequence;
	}

	/**
	 * @param accountOrderSequence
	 *            the accountOrderSequence to set
	 */
	public void setAccountOrderSequence(Long accountOrderSequence) {
		this.accountOrderSequence = accountOrderSequence;
	}

	/** 流水号 */
	private Long accountOrderSequence;

	/** 订单ID */
	private BigDecimal orderId;

	//	/** 订单信息 */
	//	private ArrayList<OrderMessageDto> orderList = new ArrayList<OrderMessageDto>();

	/**
	 * @return the result
	 */
	public boolean isResult() {
		return result;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(boolean result) {
		this.result = result;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the investmentSequence
	 */
	public Long getInvestmentSequence() {
		return investmentSequence;
	}

	/**
	 * @param investmentSequence
	 *            the investmentSequence to set
	 */
	public void setInvestmentSequence(Long investmentSequence) {
		this.investmentSequence = investmentSequence;
	}

	public BigDecimal getOrderId() {
		return orderId;
	}

	public void setOrderId(BigDecimal orderId) {
		this.orderId = orderId;
	}

}
