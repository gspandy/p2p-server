package com.vcredit.jdev.p2p.enums;

/**
 * @author ChenChang: 消息状态枚举
 */
public enum MessageStatusEnum {
	//已发布 0
	//未发布 1	
	PUBLISHED(0), UNPUBLISHED(1);

	private int code;

	public int getCode() {
		return code;
	}

	private MessageStatusEnum(int _code) {
		this.code = _code;
	}

}
