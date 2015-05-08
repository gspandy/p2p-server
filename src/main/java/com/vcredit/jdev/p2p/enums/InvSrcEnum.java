package com.vcredit.jdev.p2p.enums;

/**
 * 项目来源
 * 
 * @author zhuqiu
 *
 */
public enum InvSrcEnum {
	/** 维信VBS */
	VBS(1);

	InvSrcEnum(Integer code) {
		this.code = code;
	}

	private Integer code;

	public Integer getCode() {
		return code;
	}
}
