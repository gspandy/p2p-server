package com.vcredit.jdev.p2p.enums;

/**
 * 投资管理费率
 * 
 * @author zhuqiu
 *
 */
public enum ActInvPmfrateEnum {

	/** 投资管理费率 */
	INVEST_RATE(1);

	ActInvPmfrateEnum(Integer code) {
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
