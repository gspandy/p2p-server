package com.vcredit.jdev.p2p.enums;

/**
 * 用户充值提现记录的操作状态
 * 
 * @author zhuqiu
 *
 */
public enum ArdStatusEnum {

	/** 成功 */
	SUCCESS(0),
	/** 失败 */
	FAILURE(1);

	ArdStatusEnum(Integer code) {
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
