package com.vcredit.jdev.p2p.enums;

/**
 * 订单 记录处理状态
 * 
 * @author zhuqiu
 *
 */
public enum RecStatusEnum {
	/** 已处理 */
	TREATED(1),
	/** 未处理 */
	UNTREATED(0);

	RecStatusEnum(Integer code) {
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
