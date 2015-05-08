package com.vcredit.jdev.p2p.enums;

/**
 * 投资规则状态枚举类
 * 
 * @author zhuqiu
 *
 */
public enum AccountInvestRuleStatusEnum {

	/** 已启用 */
	ON(0),
	/** 未启用 */
	OFF(1);

	AccountInvestRuleStatusEnum(Integer code) {
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
