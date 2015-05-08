package com.vcredit.jdev.p2p.enums;

/**
 * 投资规则类别枚举类
 * 
 * @author zhuqiu
 *
 */
public enum AccountInvestRuleTypeEnum {

	/** 一键式 */
	ONE_KEY_TYPE(0),
	/** 自助式 */
	SELF_HELP(1);

	AccountInvestRuleTypeEnum(Integer code) {
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
