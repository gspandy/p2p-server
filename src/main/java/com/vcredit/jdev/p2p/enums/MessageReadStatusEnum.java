package com.vcredit.jdev.p2p.enums;

/**
 * @ClassName: MessageReadStatus
 * @Description:
 * @author ChenChang
 * @date 2015年1月6日 上午10:05:50
 */
public enum MessageReadStatusEnum {
	READ(0), //已读
	UNREAD(1);//未读

	private int code;

	MessageReadStatusEnum(int code) {
		this.code = code;
	}

	public int getCode() {
		return this.code;
	}
}
