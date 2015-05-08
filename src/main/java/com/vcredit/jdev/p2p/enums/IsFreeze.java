package com.vcredit.jdev.p2p.enums;

public enum IsFreeze {

	/** 冻结 */
	Y("Y"),
	/** 不冻结 */
	N("N");

	;

	IsFreeze(String code) {
		this.code = code;
	}

	private String code;

	public String getCode() {
		return code;
	}

	public String toString() {
		return String.valueOf(this.code);
	}
}
