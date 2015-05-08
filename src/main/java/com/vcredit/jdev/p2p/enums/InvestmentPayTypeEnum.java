package com.vcredit.jdev.p2p.enums;

/**
 * 投资项目还款方式
 * 
 * @author zhuqiu
 *
 */
public enum InvestmentPayTypeEnum {

	/** 等额本息每月还款 */
	MONTH(1),
	/** 等额本息双周还款 */
	DOUBLE_WEEK(2);

	InvestmentPayTypeEnum(Integer code) {
		this.code = code;
	}

	private Integer code;

	public Integer getCode() {
		return code;
	}

	public String toString() {
		return String.valueOf(this.code);
	}
}
