package com.vcredit.jdev.p2p.dto;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * 贷款人还款数据传输类
 * 
 * @author zhuqiu
 *
 */
public class BondPayOffBackMessageDto {

	/** 处理结果 */
	private boolean result;
	/** 信息 */
	private String msg;
	/** 项目流水号 */
	private Long investmentSequence;
	/** 项目期次 */
	private Integer peroidOfTime;
	/** 贷款用户 */
	private Long payAccountSequence;
	/** 贷款用户 */
	BigDecimal paymentAmount;

	public Long getPayAccountSequence() {
		return payAccountSequence;
	}

	public void setPayAccountSequence(Long payAccountSequence) {
		this.payAccountSequence = payAccountSequence;
	}

	public Integer getPeroidOfTime() {
		return peroidOfTime;
	}

	public void setPeroidOfTime(Integer peroidOfTime) {
		this.peroidOfTime = peroidOfTime;
	}

	public Long getAccountOrderSequence() {
		return accountOrderSequence;
	}

	public void setAccountOrderSequence(Long accountOrderSequence) {
		this.accountOrderSequence = accountOrderSequence;
	}

	/** 流水号 */
	private Long accountOrderSequence;

	/** 订单信息 */
	private ArrayList<OrderMessageDto> orderList = new ArrayList<OrderMessageDto>();

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
	 * @return the orderList
	 */
	public ArrayList<OrderMessageDto> getOrderList() {
		return orderList;
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
	 * @param orderList
	 *            the orderList to set
	 */
	public void setOrderList(ArrayList<OrderMessageDto> orderList) {
		this.orderList = orderList;
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
}
