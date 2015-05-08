package com.vcredit.jdev.p2p.enums;
/**
 * 
 * @author zhaopeijun
 *
 */
public enum InvestSourceEnum {
	
	/** 维信 */
	SOURCE_WEIXIN(1,"维信");
	
	/** 批量 */
	
	InvestSourceEnum(Integer code,String value) {
		this.code = code;
		this.value = value;
	}

	private Integer code;
	
	private String value;

	public Integer getCode() {
		return code;
	}

	public String getValue() {
		return value;
	}
	
}
