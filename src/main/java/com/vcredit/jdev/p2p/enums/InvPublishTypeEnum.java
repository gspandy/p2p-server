package com.vcredit.jdev.p2p.enums;
/**
 * 
 * @author zhaopeijun
 *
 */
public enum InvPublishTypeEnum {
	
	/** 自动 */
	publish_auto(1),
	
	/** 定时 */
	publish_timing(2);
	
	InvPublishTypeEnum(int code) {
		this.code = code;
	}

	private int code;

	public int getCode() {
		return code;
	}
}
