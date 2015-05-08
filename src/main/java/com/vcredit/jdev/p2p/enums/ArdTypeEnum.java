package com.vcredit.jdev.p2p.enums;

/**
 * 用户充值提现记录的操作类型
 * 
 * @author zhuqiu
 *
 */
public enum ArdTypeEnum {

	/** 充值 */
	RECHARGE(0),
	/** 提现 */
	WITHDRAW(1);

	ArdTypeEnum(Integer code) {
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
