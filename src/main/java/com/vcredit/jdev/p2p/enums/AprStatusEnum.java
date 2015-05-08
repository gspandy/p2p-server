package com.vcredit.jdev.p2p.enums;

/**
 * 缴费状态
 * 
 * @author zhuqiu
 *
 */
public enum AprStatusEnum {

	/** 未缴费 */
	NOT_PAID(1),
	/** 缴费中 */
	PAYING(2),
	/** 缴费成功 */
	PAY_SUCCESS(3),
	/** 缴费失败 */
	PAY_FAILURE(4),

	;

	AprStatusEnum(Integer code) {
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
