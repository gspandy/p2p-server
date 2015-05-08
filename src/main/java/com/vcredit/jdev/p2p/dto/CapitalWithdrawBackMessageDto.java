package com.vcredit.jdev.p2p.dto;

/**
 * 提现消息数据类
 * 
 * @author zhuqiu
 *
 */
public class CapitalWithdrawBackMessageDto {

	/** 处理结果 */
	private boolean result;
	/** 信息 */
	private String msg;
	/** 项目流水号 */
	private Long investmentSequence;
	/** 用户充值/提现记录 流水号 */
	private Long cashOperationRecordSequence;//	流水号
	/** 流水号 */
	private Long accountOrderSequence;

	/**
	 * @return the cashOperationRecordSequence
	 */
	public Long getCashOperationRecordSequence() {
		return cashOperationRecordSequence;
	}

	/**
	 * @param cashOperationRecordSequence
	 *            the cashOperationRecordSequence to set
	 */
	public void setCashOperationRecordSequence(Long cashOperationRecordSequence) {
		this.cashOperationRecordSequence = cashOperationRecordSequence;
	}

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
}
