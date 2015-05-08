package com.vcredit.jdev.p2p.enums;

public enum ThirdPaymentAccountBindStatusEnum {
	/** 已绑定*/
	IS_BIND(0),
	/** 未绑定 */
	IS_NO_BIND(1);

	ThirdPaymentAccountBindStatusEnum(Integer code) {
		this.code = code;
	}

	private Integer code;

	public Integer getCode() {
		return code;
	}
}
