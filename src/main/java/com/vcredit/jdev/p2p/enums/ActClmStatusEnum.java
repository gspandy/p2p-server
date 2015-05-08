package com.vcredit.jdev.p2p.enums;

/**
 * 用户获得的债权状态
 * 
 * @author zhuqiu
 *
 */
public enum ActClmStatusEnum {

	/** 还款中 */
	REPAYMENT(1),
	/** 转让中 */
	TRANSFERING(2),
	/** 已转让 */
	TRANSFERED(3),
	/** 逾期 */
	OVERDUE(4),
	/** 已结清 */
	CLEARED(5),
	/** 未决 */
	NOT_SETTLED(6);

	ActClmStatusEnum(Integer code) {
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
