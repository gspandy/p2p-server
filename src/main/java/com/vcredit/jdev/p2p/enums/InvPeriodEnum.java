package com.vcredit.jdev.p2p.enums;

/**
 * 投资项目期限枚举类
 * 
 * @author zhuqiu
 *
 */
public enum InvPeriodEnum {

	/** 6 */
	PERIOD_SIX(1),
	/** 12 */
	PERIOD_TWELVE(2),
	/** 18 */
	PERIOD_EIGHTEEN(3),
	/** 24 */
	PERIOD_TWENTY_FOUR(4),
	/** 36 */
	PERIOD_THIRTY_SIX(5), ;

	InvPeriodEnum(Integer code) {
		this.code = code;
	}

	private Integer code;

	public Integer getCode() {
		return code;
	}

}
