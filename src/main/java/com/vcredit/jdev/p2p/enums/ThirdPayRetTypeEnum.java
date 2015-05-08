package com.vcredit.jdev.p2p.enums;

/**
 * 三方支付还款方式
 * 
 * @author 周佩
 *
 */
public enum ThirdPayRetTypeEnum {
	/** 01：等额本息  */
	_01("01"),
	/** 02：等额本金 */
	_02("02"),
	/** 03：按期付息，到期还本 */
	_03("03"),
	/** 04：一次性还款 */
	_04("04"),
	/** 99：其他 */
	_99("99");

	ThirdPayRetTypeEnum(String code) {
		this.code = code;
	}

	private String code;

	public String getCode() {
		return code;
	}

	public String toString() {
		return this.code;
	}
}
