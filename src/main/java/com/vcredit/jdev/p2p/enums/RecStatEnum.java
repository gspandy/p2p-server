package com.vcredit.jdev.p2p.enums;

/**
 * 小贷公司扣款结果处理状态
 * 
 * @author zhuqiu
 *
 */
public enum RecStatEnum {
	/** 已处理 */
	TREATED(0),
	/** 未处理 */
	UNTREATED(1);

	RecStatEnum(Integer code) {
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
