package com.vcredit.jdev.p2p.enums;

/**
 * @author ChenChang: 消息类型枚举
 */
public enum MessageTypeEnum {
	//消息 Message
	//公告 Announcement
	//活动 Event
	//广告 Advertisement
	//通知 Notice
	Message(1), Announcement(2), Event(3), Advertisement(4), Notice(5);

	private int code;

	public int getCode() {
		return code;
	}

	private MessageTypeEnum(int _code) {
		this.code = _code;
	}

}
