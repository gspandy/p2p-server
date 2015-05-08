package com.vcredit.jdev.p2p.enums;

public enum ThirdPaymentAccountActiveStatusEnum {
	/** 已激活*/
	IS_ACTIVE(0),
	/** 未激活 */
	IS_NO_ACTIVE(1);

	ThirdPaymentAccountActiveStatusEnum(Integer code) {
		this.code = code;
	}

	private Integer code;

	public Integer getCode() {
		return code;
	}
}
