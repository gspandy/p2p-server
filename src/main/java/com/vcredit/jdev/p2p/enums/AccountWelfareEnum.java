package com.vcredit.jdev.p2p.enums;

public enum AccountWelfareEnum {
	/** 未兑换 */
	NO_EXCHANGE(1),
	/** 已兑换 */
	EXCHANGE(2),
	/** 已过期 */
	EXPIRED(3),
	/** 提现 */
	DEPOSIT(1),
	/** 投资 */
	INVESTMENT(2),;

	AccountWelfareEnum(Integer code) {
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
