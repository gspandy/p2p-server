package com.vcredit.jdev.p2p.dto;

import java.util.ArrayList;

/**
 * 放款订阅数据传输类
 * 
 * @author zhuqiu
 *
 */
public class UnFreezeApplyBackMessageDto {

	/** 处理结果 */
	private boolean result;
	/** 信息 */
	private String msg;
	/** 项目流水号 */
	private Long investmentSequence;

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
