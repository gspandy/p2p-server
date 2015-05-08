package com.vcredit.jdev.p2p.enums;
/**
 * VBS推送方式
 * @author zhaopeijun
 *
 */
public enum BizSyncEnum {
	
	/** 实时、单条推送,临时  */
	sync_instant("1"),
	
	/** 批量、可多条推送,常规*/
	sync_batch("2");
	
	BizSyncEnum(String code) {
		this.code = code;
	}

	private String code;

	public String getCode() {
		return code;
	}
}
