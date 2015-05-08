package com.vcredit.jdev.p2p.enums;


/** 
 * @ClassName:  MessageIssueTypeEnum
 * @Description:  消息发布方式
 * @author ChenChang 
 * @date 2014年12月31日 下午3:15:38
 */
public enum MessageIssueTypeEnum {
	//自动发布 0
	//定时发布 1
	//手动发布 2
	AUTO(0), SCHEDULE(1), MANUAL(2);

	private int code;

	public int getCode() {
		return code;
	}

	private MessageIssueTypeEnum(int _code) {
		this.code = _code;
	}

}
