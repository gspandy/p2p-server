package com.vcredit.jdev.p2p.enums;

/**
 * 商户私有域 状态
 * 
 * @author zhuqiu
 *
 */
public enum MerPrivEnum {

	/** 正常还款 */
	NORMAL_PAID("NORMALPAID"),
	/** 垫付 */
	PREPAID("PREPAID"),

	;

	MerPrivEnum(String code) {
		this.code = code;
	}

	private String code;

	public String getCode() {
		return code;
	}

}
