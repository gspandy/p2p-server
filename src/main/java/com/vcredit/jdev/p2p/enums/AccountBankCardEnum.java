package com.vcredit.jdev.p2p.enums;

public enum AccountBankCardEnum {
	/** 默认银行卡 */
	DEFAULT_CARD(0),
	/** 非默认银行卡 */
	NOT_DEFAULT_CARD(1);
	AccountBankCardEnum(Integer code) {
		this.code = code;
	}

	private Integer code;

	public Integer getCode() {
		return code;
	}
}
