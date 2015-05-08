package com.vcredit.jdev.p2p.enums;

/**
 * 债权还款计划状态
 * 
 * @author zhuqiu
 *
 */
public enum ClaimPayPlanStatusEnum {

	/** 未还 */
	NOT_PAID(1),
	/** 未决 */
	NOT_SETTLED(2),
	/** 逾期未垫付 */
	OVERDUE(3),
	/** 已还 */
	PAID(4),
	/** 逾期已垫付 */
	OVERDUE_PAID(5),
	/** 还款中 */
	PAYING(6),
	/** 还款失败 */
	PAY_FAILURE(7),
	/** VBS已还 */
	VBS_PAID(8), ;

	ClaimPayPlanStatusEnum(Integer code) {
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
