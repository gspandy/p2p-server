package com.vcredit.jdev.p2p.enums;

/**
 * 操作类型 常量
 * 
 * @author zhuqiu
 *
 */
public enum OpTypeEnum {

	/** 开始自动投资 */
	AUTO_INVEST_BEGIN(1),
	/** 结束自动投资 */
	AUTO_INVEST_END(2), ;

	OpTypeEnum(Integer code) {
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
