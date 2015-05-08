package com.vcredit.jdev.p2p.dto;


/**
 * 放款订阅数据传输类
 * 
 * @author zhuqiu
 *
 */
public class ManualInvestBackMessageDto {

	/** 处理结果 */
	private boolean result;
	/** 信息 */
	private String msg;
	/** 项目流水号 */
	private Long investmentSequence;

	/** 订单信息 */
	private Long accountOrderSequence;// 流水号

	/** 订单号 */
	private String orderId;

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

	public Long getAccountOrderSequence() {
		return accountOrderSequence;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setAccountOrderSequence(Long accountOrderSequence) {
		this.accountOrderSequence = accountOrderSequence;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
