package com.vcredit.jdev.p2p.enums;

/**
 * 债权收款计划状态
 * 
 * @author zhuqiu
 *
 */
public enum ClaimGatherPlanStatusEnum {

	/** 未收 */
	NOT_PAID(1),
	/** 未决 */
	NOT_SETTLED(2),
	/** 逾期未垫付 */
	OVERDUE(3),
	/** 已收 */
	PAID(4),
	/** 收款中 */
	GATHERING(5),
	/** 收款失败 */
	PAID_FAILURE(6),

	;

	ClaimGatherPlanStatusEnum(Integer code) {
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
