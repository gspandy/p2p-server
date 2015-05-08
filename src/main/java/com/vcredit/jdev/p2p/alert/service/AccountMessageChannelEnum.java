package com.vcredit.jdev.p2p.alert.service;

/**
 * @ClassName: AccountMessageTemplate
 * @Description:
 * @author ChenChang
 * @date 2014年12月26日 下午3:34:45
 */
public enum AccountMessageChannelEnum {

	EMAIL(1), //邮件
	MOBILE(2), //手机短信
	SITE(3);//站内信

	private int code;

	public int getCode() {
		return code;
	}

	private AccountMessageChannelEnum(int _code) {
		this.code = _code;
	}

	@Override
	public String toString() {
		return String.valueOf(this.code);
	}

}
