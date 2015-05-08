package com.vcredit.jdev.p2p.enums;

public enum ResourceEnum {

	/** 找到*/
	IS_FIND(0),
	
	/** 未找到*/
	IS_NO_FIND(1);
	
	ResourceEnum(Integer code){
		this.code = code;
	}
	
	private Integer code;

	public Integer getCode() {
		return code;
	} 
	
}
