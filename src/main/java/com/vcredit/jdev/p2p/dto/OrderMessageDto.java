package com.vcredit.jdev.p2p.dto;

import java.math.BigDecimal;

public class OrderMessageDto {

	public OrderMessageDto(boolean result, BigDecimal accountOrderSequence) {
		super();
		this.result = result;
		this.accountOrderSequence = accountOrderSequence;
	}

	/** 处理结果 */
	private boolean result;

	/**
	 * @return the result
	 */
	public boolean isResult() {
		return result;
	}

	/**
	 * @return the accountOrderSequence
	 */
	public BigDecimal getAccountOrderSequence() {
		return accountOrderSequence;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(boolean result) {
		this.result = result;
	}

	/**
	 * @param accountOrderSequence
	 *            the accountOrderSequence to set
	 */
	public void setAccountOrderSequence(BigDecimal accountOrderSequence) {
		this.accountOrderSequence = accountOrderSequence;
	}

	/** 流水号 */
	private BigDecimal accountOrderSequence;

}
